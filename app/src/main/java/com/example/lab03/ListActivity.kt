package com.example.lab03

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListActivity : AppCompatActivity() {

    private lateinit var listViewSubjects: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val subjectList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_activity)

        // Khởi tạo ListView
        listViewSubjects = findViewById(R.id.list)

        // Khởi tạo ArrayAdapter với layout tùy chỉnh
        adapter = ArrayAdapter(this, R.layout.item_list, subjectList)
        listViewSubjects.adapter = adapter

        // Kết nối Firebase Database
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://colege-f7777-default-rtdb.firebaseio.com/subjects")

        // Lắng nghe sự thay đổi dữ liệu từ Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                subjectList.clear() // Xóa dữ liệu cũ trước khi cập nhật
                for (subjectSnapshot in snapshot.children) {
                    val subjectName = subjectSnapshot.child("name").getValue(String::class.java)
                    subjectName?.let {
                        subjectList.add(it)
                    }
                }
                adapter.notifyDataSetChanged() // Cập nhật ListView
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu có
            }
        })
    }
}
