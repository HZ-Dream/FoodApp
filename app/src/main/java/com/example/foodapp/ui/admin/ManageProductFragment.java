package com.example.foodapp.ui.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.foodapp.databinding.ManageProductsBinding;
import com.example.foodapp.datas.ProductsDAO;
import com.example.foodapp.datas.CategoriesDAO;
import com.example.foodapp.models.Products;
import com.example.foodapp.models.Categories;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManageProductFragment extends Fragment {

    private ManageProductsBinding binding;
    private ProductsDAO productsDAO;
    private CategoriesDAO categoriesDAO;
    private List<Products> productList;
    private Products selectedProduct = null;
    private String selectedImageName = null;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    private List<Categories> categoryList;
    private ArrayAdapter<String> categoryAdapter;
    private int selectedCategoryId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ManageProductsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productsDAO = new ProductsDAO(getContext());
        categoriesDAO = new CategoriesDAO(getContext());

        setupImagePicker();
        loadCategories();
        setupListView();
        setupClickListeners();
        loadProducts();
        resetUI();
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            binding.imgProduct.setImageURI(imageUri);
                            selectedImageName = saveImageToInternalStorage(imageUri);
                        }
                    }
                });
    }

    private void loadCategories() {
        categoryList = categoriesDAO.getAllCategories();
        List<String> names = new ArrayList<>();
        for (Categories c : categoryList) {
            names.add(c.getName());
        }
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, names);
        binding.spnCategory.setAdapter(categoryAdapter);

        binding.spnCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedCategoryId = categoryList.get(position).getId();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedCategoryId = -1;
            }
        });
    }

    private void setupListView() {
        productList = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        binding.lvProducts.setAdapter(adapter);

        binding.lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            selectedProduct = productList.get(position);
            binding.edtName.setText(selectedProduct.getName());
            binding.edtPrice.setText(String.valueOf(selectedProduct.getPrice()));
            binding.edtTimeCook.setText(selectedProduct.getTimeCook());
            loadImageFromInternalStorage(selectedProduct.getImage());

            int categoryPosition = findCategoryPosition(selectedProduct.getCatId());
            if (categoryPosition >= 0) binding.spnCategory.setSelection(categoryPosition);

            binding.btnAdd.setEnabled(false);
            binding.btnEdit.setEnabled(true);
            binding.btnDelete.setEnabled(true);
        });
    }

    private void setupClickListeners() {
        binding.btnChangeImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        binding.btnAdd.setOnClickListener(v -> addProduct());
        binding.btnEdit.setOnClickListener(v -> editProduct());
        binding.btnDelete.setOnClickListener(v -> deleteProduct());
        binding.btnShow.setOnClickListener(v -> showProduct());
        binding.btnShowAll.setOnClickListener(v -> loadProducts());
    }

    private void loadProducts() {
        productList.clear();
        productList.addAll(productsDAO.getAllProducts());

        List<String> names = new ArrayList<>();
        for (Products p : productList) names.add(p.getName());
        ((ArrayAdapter<String>) binding.lvProducts.getAdapter()).clear();
        ((ArrayAdapter<String>) binding.lvProducts.getAdapter()).addAll(names);
        ((ArrayAdapter<String>) binding.lvProducts.getAdapter()).notifyDataSetChanged();
    }

    private void addProduct() {
        String name = binding.edtName.getText().toString().trim();
        String timeCook = binding.edtTimeCook.getText().toString().trim();
        String priceStr = binding.edtPrice.getText().toString().trim();

        if (name.isEmpty() || timeCook.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategoryId == -1) {
            Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageName == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        Products p = new Products(name, price, timeCook, selectedImageName, selectedCategoryId);
        long result = productsDAO.addProduct(p);

        if (result > 0) {
            Toast.makeText(getContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
            loadProducts();
            resetUI();
        } else {
            Toast.makeText(getContext(), "Add failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void editProduct() {
        if (selectedProduct == null) return;

        String name = binding.edtName.getText().toString().trim();
        String timeCook = binding.edtTimeCook.getText().toString().trim();
        String priceStr = binding.edtPrice.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(getContext(), "Name and Price required", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String imageName = (selectedImageName != null) ? selectedImageName : selectedProduct.getImage();

        selectedProduct.setName(name);
        selectedProduct.setTimeCook(timeCook);
        selectedProduct.setPrice(price);
        selectedProduct.setImage(imageName);
        selectedProduct.setCatId(selectedCategoryId);

        int result = productsDAO.updateProduct(selectedProduct);
        if (result > 0) {
            Toast.makeText(getContext(), "Product updated!", Toast.LENGTH_SHORT).show();
            loadProducts();
            resetUI();
        } else {
            Toast.makeText(getContext(), "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteProduct() {
        if (selectedProduct == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Product")
                .setMessage("Delete '" + selectedProduct.getName() + "'?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteImageFromInternalStorage(selectedProduct.getImage());
                    int result = productsDAO.deleteProduct(selectedProduct.getId());
                    if (result > 0) {
                        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                        loadProducts();
                        resetUI();
                    } else {
                        Toast.makeText(getContext(), "Delete failed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showProduct() {
        if (selectedCategoryId == -1) {
            Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            return;
        }

        productList.clear();
        productList.addAll(productsDAO.getProductsByCategoryId(selectedCategoryId));

        List<String> names = new ArrayList<>();
        for (Products p : productList) names.add(p.getName());

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.lvProducts.getAdapter();
        adapter.clear();
        adapter.addAll(names);
        adapter.notifyDataSetChanged();

        if (names.isEmpty()) {
            Toast.makeText(getContext(), "No products found for this category", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetUI() {
        selectedProduct = null;
        selectedImageName = null;
        binding.edtName.setText("");
        binding.edtPrice.setText("");
        binding.edtTimeCook.setText("");
        binding.imgProduct.setImageResource(android.R.drawable.ic_menu_gallery);
        binding.btnAdd.setEnabled(true);
        binding.btnEdit.setEnabled(false);
        binding.btnDelete.setEnabled(false);
    }

    private int findCategoryPosition(int categoryId) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == categoryId)
                return i;
        }
        return -1;
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            String name = binding.edtName.getText().toString().trim().replaceAll("\\s+", "_");
            if (name.isEmpty()) name = "product";
            String timeStamp = new SimpleDateFormat("ddMMyyyyHHmm", Locale.getDefault()).format(new Date());
            String fileName = name + "_" + timeStamp + ".jpg";

            File file = new File(getContext().getFilesDir(), fileName);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                outputStream.write(buffer, 0, length);

            outputStream.close();
            inputStream.close();
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadImageFromInternalStorage(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            binding.imgProduct.setImageResource(android.R.drawable.ic_menu_gallery);
            return;
        }
        try {
            File file = new File(getContext().getFilesDir(), fileName);
            if (file.exists()) binding.imgProduct.setImageURI(Uri.fromFile(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteImageFromInternalStorage(String fileName) {
        if (fileName == null || fileName.isEmpty()) return;
        try {
            File file = new File(getContext().getFilesDir(), fileName);
            if (file.exists()) file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
