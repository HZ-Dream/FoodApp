package com.example.foodapp.ui.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.foodapp.R;
import com.example.foodapp.datas.UsersDAO;
import com.example.foodapp.models.Users;
import java.util.ArrayList;
import java.util.List;

public class ManageUsersFragment extends Fragment {

    private ListView listViewUsers;
    private Button btnAddUser;
    private UsersDAO usersDAO;
    private ArrayAdapter<String> adapter;
    private List<Users> usersList;
    private List<String> userNames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_users, container, false);

        listViewUsers = view.findViewById(R.id.listViewUsers);
        btnAddUser = view.findViewById(R.id.btnAddUser);

        usersDAO = new UsersDAO(getContext());
        loadUsers();

        listViewUsers.setOnItemClickListener((parent, v, position, id) -> showUserOptionsDialog(usersList.get(position)));
        btnAddUser.setOnClickListener(v -> showAddUserDialog());

        return view;
    }

    private void loadUsers() {
        usersList = usersDAO.getAllUsersList();
        userNames = new ArrayList<>();
        for (Users user : usersList) {
            String role = user.isAdmin() ? " (Admin)" : "";
            userNames.add(user.getName() + " - " + user.getEmail() + role);
        }
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, userNames);
        listViewUsers.setAdapter(adapter);
    }

    private void showUserOptionsDialog(Users user) {
        String[] options = {"Chỉnh sửa", "Xóa", user.isAdmin() ? "Hủy quyền Admin" : "Cấp quyền Admin"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tùy chọn cho " + user.getName());
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    showEditUserDialog(user);
                    break;
                case 1:
                    usersDAO.deleteUser(user.getId());
                    loadUsers();
                    break;
                case 2:
                    usersDAO.setAdmin(user.getId(), !user.isAdmin());
                    loadUsers();
                    break;
            }
        });
        builder.show();
    }

    private void showAddUserDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);
        CheckBox chkAdmin = dialogView.findViewById(R.id.chkAdmin);

        new AlertDialog.Builder(getContext())
                .setTitle("Thêm người dùng mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", (dialog, which) -> {
                    Users newUser = new Users(
                            edtEmail.getText().toString(),
                            edtName.getText().toString(),
                            edtPhone.getText().toString(),
                            edtPassword.getText().toString(),
                            chkAdmin.isChecked()
                    );
                    usersDAO.addUser(newUser);
                    loadUsers();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditUserDialog(Users user) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);

        EditText edtName = dialogView.findViewById(R.id.edtName);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        EditText edtPhone = dialogView.findViewById(R.id.edtPhone);
        EditText edtPassword = dialogView.findViewById(R.id.edtPassword);
        CheckBox chkAdmin = dialogView.findViewById(R.id.chkAdmin);

        edtName.setText(user.getName());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhone());
        edtPassword.setText(user.getPassword());
        chkAdmin.setChecked(user.isAdmin());

        new AlertDialog.Builder(getContext())
                .setTitle("Chỉnh sửa người dùng")
                .setView(dialogView)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    user.setName(edtName.getText().toString());
                    user.setEmail(edtEmail.getText().toString());
                    user.setPhone(edtPhone.getText().toString());
                    user.setPassword(edtPassword.getText().toString());
                    user.setAdmin(chkAdmin.isChecked());
                    usersDAO.updateUser(user);
                    usersDAO.setAdmin(user.getId(), chkAdmin.isChecked());
                    loadUsers();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
