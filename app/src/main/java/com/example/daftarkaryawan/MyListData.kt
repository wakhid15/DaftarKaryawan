package com.example.daftarkaryawan

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity(), RecyclerViewAdapter.data_listener {
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    val database = FirebaseDatabase.getInstance()
    private var dataKaryawan = ArrayList<data_karyawan>()
    private var auth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)
        recyclerView = findViewById(R.id.datalist)
        supportActionBar!!.title = "Data Karyawan"
        auth  = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }
    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Bersabar :)", Toast.LENGTH_LONG).show()
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("DataKaryawan")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged", "ShowToast")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()){
                        dataKaryawan.clear()
                        for (snapshot in dataSnapshot.children){
                            val karyawan = snapshot.getValue(data_karyawan::class.java)
                            karyawan?.key = snapshot.key
                            dataKaryawan.add(karyawan!!)
                        }
                        adapter = RecyclerViewAdapter(dataKaryawan,this@MyListData)
                        recyclerView?.adapter = adapter
                        (adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Data Berhasil Dimuat",
                            Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(applicationContext,"Data Gagal Dimuat",Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }
    private fun MyRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext,R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }


    override fun OnDeleteData(data: data_karyawan?, position: Int) {
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getrefrence = database.getReference()

        if (getrefrence != null){

            getrefrence.child("Admin").child(getUserID).child("DataKaryawan").child(data?.key.toString()).removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@MyListData,  "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext,MyListData::class.java)
                    startActivity(intent)
                    finish()
                }} else {
            Toast.makeText(this@MyListData, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show()
            }
        }

}
