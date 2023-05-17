package com.example.daftarkaryawan

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val listdata_karyawan: ArrayList<data_karyawan>, context: Context):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
        private val context: Context

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val Nama : TextView
        val Nip : TextView
        val Jabatan : TextView
        val Jkel : TextView
        val ListItem : LinearLayout
        init {
            Nama = itemView.findViewById(R.id.nama)
            Nip = itemView.findViewById(R.id.nip)
            Jabatan = itemView.findViewById(R.id.jabatan)
            Jkel = itemView.findViewById(R.id.jkel)
            ListItem = itemView.findViewById(R.id.list_item)
        }

    }

    var listener: data_listener? = null
    interface data_listener {
        fun OnDeleteData(data:data_karyawan?, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V:View = LayoutInflater.from(parent.context).inflate(R.layout.view_design,
            parent, false)
        return ViewHolder(V)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val Nama : String? = listdata_karyawan.get(position).nama
        val Nip : String? = listdata_karyawan.get(position).nip
        val Jabatan : String? = listdata_karyawan.get(position).jabatan
        val Jkel : String? = listdata_karyawan.get(position).jkel

        holder.Nama.text = "Nama : $Nama"
        holder.Nip.text = "Nomor Induk Pegawai : $Nip"
        holder.Jabatan.text = "Jabatan : $Jabatan"
        holder.Jkel.text = "Jenis Kelamin : $Jkel"
        holder.ListItem.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                holder.ListItem.setOnLongClickListener {view->
                    val action = arrayOf("Update","Delete")
                    val alert: AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alert.setItems(action, DialogInterface.OnClickListener{ dialog, i ->
                        when (i) {
                            0-> {
                                val bundle = Bundle()
                                bundle.putString("dataNama",listdata_karyawan[position].nama)
                                bundle.putString("dataNip",listdata_karyawan[position].nip)
                                bundle.putString("dataJabatan",listdata_karyawan[position].jabatan)
                                bundle.putString("dataJkel",listdata_karyawan[position].jkel)
                                bundle.putString("getPrimaryKey",listdata_karyawan[position].key)
                                val intent = Intent(view.context, UpdateData::class.java)
                                intent.putExtras(bundle)
                                context.startActivity(intent)
                            }
                            1->{
                                listener?.OnDeleteData(listdata_karyawan[position],position)
                            }
                        }
                    })
                    alert.create()
                    alert.show()
                    true
                }
                return true
            }
            })

    }
    override fun getItemCount(): Int {
        return listdata_karyawan.size
    }

    init {
        this.context = context
        this.listener= context as MyListData
        }

}
