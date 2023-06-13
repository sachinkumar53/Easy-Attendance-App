package com.ajstudios.easyattendance.Adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.realm.Class_Names
import com.ajstudios.easyattendance.realm.Students_List
import com.ajstudios.easyattendance.viewholders.ViewHolder
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults

class ClassListAdapter(
    private val list: RealmResults<Class_Names>,
    private val activity: Activity
) : RealmRecyclerViewAdapter<Class_Names?, ViewHolder>(activity, list, true) {
    private val realm = Realm.getDefaultInstance()
    private var realmChangeListener: RealmChangeListener<Realm>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.class_adapter, parent, false)
        return ViewHolder(itemView, activity, list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val temp = getItem(position)
        Realm.init(activity)

        realmChangeListener = RealmChangeListener {
            val count = realm.where(Students_List::class.java)
                .equalTo("class_id", temp!!.id)
                .count()
            holder.total_students.text = "Students : $count"
        }

        realm.addChangeListener(realmChangeListener)
        val count = realm.where(Students_List::class.java)
            .equalTo("class_id", temp!!.id)
            .count()

        holder.total_students.text = "Students : $count"
        holder.class_name.text = temp.name_class
        holder.subject_name.text = temp.name_subject

        when (temp.position_bg) {
            "0" -> {
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleblue)
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_1)
            }

            "1" -> {
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_green)
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_2)
            }

            "2" -> {
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_yellow)
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_3)
            }

            "3" -> {
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_palegreen)
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_4)
            }

            "4" -> {
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_paleorange)
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_5)
            }

            "5" -> {
                holder.imageView_bg.setImageResource(R.drawable.asset_bg_white)
                holder.frameLayout.setBackgroundResource(R.drawable.gradient_color_6)
                holder.subject_name.setTextColor(holder.itemView.context.resources.getColor(R.color.text_color_secondary))
                holder.class_name.setTextColor(holder.itemView.context.resources.getColor(R.color.text_color_secondary))
                holder.total_students.setTextColor(holder.itemView.context.resources.getColor(R.color.text_color_secondary))
            }
        }
    }
}