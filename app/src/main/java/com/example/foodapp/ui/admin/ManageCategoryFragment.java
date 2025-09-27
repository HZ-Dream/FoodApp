package com.example.foodapp.ui.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.foodapp.adapters.CategoryAdapter;
import com.example.foodapp.databinding.ManageCategoriesBinding;
import com.example.foodapp.datas.CategoriesDAO;
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

public class ManageCategoryFragment extends Fragment {

    private ManageCategoriesBinding binding;
    private CategoriesDAO categoriesDAO;
    private CategoryAdapter adapter;
    private List<Categories> categoryList;
    private Categories selectedCategory = null;
    private String selectedImageName = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ManageCategoriesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriesDAO = new CategoriesDAO(getContext());
        setupImagePicker();
        setupListView();
        setupClickListeners();
        loadCategories();
        resetUIState();
    }

    private void setupImagePicker() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            binding.imageCategoryView.setImageURI(imageUri);
                            selectedImageName = saveImageToInternalStorage(imageUri);
                        }
                    }
                });
    }

    private void setupListView() {
        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(getContext(), categoryList);
        binding.lvListCategory.setAdapter(adapter);

        binding.lvListCategory.setOnItemClickListener((parent, view, position, id) -> {
            selectedCategory = categoryList.get(position);

            binding.edtNameCategory.setText(selectedCategory.getName());

            loadImageFromInternalStorage(selectedCategory.getImage());

            binding.btnAddCategory.setEnabled(false);
            binding.btnEditCategory.setEnabled(true);
            binding.btnDeleteCategory.setEnabled(true);
        });
    }

    private void setupClickListeners() {
        binding.btnImgCategory.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        binding.btnAddCategory.setOnClickListener(v -> addCategory());

        binding.btnEditCategory.setOnClickListener(v -> editCategory());

        binding.btnDeleteCategory.setOnClickListener(v -> {
            if (categoriesDAO.checkProductExists(selectedCategory.getId())) {
                Toast.makeText(getContext(), "Cannot delete category with associated products", Toast.LENGTH_SHORT).show();
                return;
            }

            deleteCategory();
        });
    }

    private void loadCategories() {
        categoryList.clear();
        categoryList.addAll(categoriesDAO.getAllCategories());
        adapter.notifyDataSetChanged();
    }

    private void resetUIState() {
        selectedCategory = null;
        selectedImageName = null;
        binding.edtNameCategory.setText("");
        binding.imageCategoryView.setImageResource(android.R.drawable.ic_menu_gallery);
        binding.btnAddCategory.setEnabled(true);
        binding.btnEditCategory.setEnabled(false);
        binding.btnDeleteCategory.setEnabled(false);
        binding.lvListCategory.clearChoices();
        adapter.notifyDataSetChanged();
    }

    private void addCategory() {
        String name = binding.edtNameCategory.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageName == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        Categories newCategory = new Categories(name, selectedImageName);
        long result = categoriesDAO.addCategory(newCategory);
        if (result != -1) {
            Toast.makeText(getContext(), "Category added successfully!", Toast.LENGTH_SHORT).show();
            loadCategories();
            resetUIState();
        } else {
            Toast.makeText(getContext(), "Failed to add category", Toast.LENGTH_SHORT).show();
        }
    }

    private void editCategory() {
        if (selectedCategory == null) return;

        String newName = binding.edtNameCategory.getText().toString().trim();
        if (newName.isEmpty()) {
            Toast.makeText(getContext(), "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageName = (selectedImageName != null) ? selectedImageName : selectedCategory.getImage();

        selectedCategory.setName(newName);
        selectedCategory.setImage(imageName);

        int result = categoriesDAO.updateCategory(selectedCategory);
        if (result > 0) {
            Toast.makeText(getContext(), "Category updated successfully!", Toast.LENGTH_SHORT).show();
            loadCategories();
            resetUIState();
        } else {
            Toast.makeText(getContext(), "Failed to update category", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCategory() {
        if (selectedCategory == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete '" + selectedCategory.getName() + "'?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteImageFromInternalStorage(selectedCategory.getImage());

                    int result = categoriesDAO.deleteCategory(selectedCategory.getId());
                    if (result > 0) {
                        Toast.makeText(getContext(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                        loadCategories();
                        resetUIState();
                    } else {
                        Toast.makeText(getContext(), "Failed to delete category", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            String name = binding.edtNameCategory.getText().toString().trim().replaceAll("\\s+", "_");
            if (name.isEmpty()) name = "category";
            String timeStamp = new SimpleDateFormat("ddMMyyyyHHmm", Locale.getDefault()).format(new Date());
            String fileName = name + "_" + timeStamp + ".jpg";

            File file = new File(getContext().getFilesDir(), fileName);
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

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
            binding.imageCategoryView.setImageResource(android.R.drawable.ic_menu_gallery);
            return;
        }
        try {
            File file = new File(getContext().getFilesDir(), fileName);
            if (file.exists()) {
                binding.imageCategoryView.setImageURI(Uri.fromFile(file));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteImageFromInternalStorage(String fileName) {
        if (fileName == null || fileName.isEmpty()) return;
        try {
            File file = new File(getContext().getFilesDir(), fileName);
            if (file.exists()) {
                file.delete();
            }
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