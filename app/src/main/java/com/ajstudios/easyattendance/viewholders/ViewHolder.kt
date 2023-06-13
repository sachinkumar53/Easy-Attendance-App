package com.ajstudios.easyattendance.viewholders

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.ajstudios.easyattendance.ClassDetailActivity
import com.ajstudios.easyattendance.R
import com.ajstudios.easyattendance.realm.Class_Names
import com.google.android.material.card.MaterialCardView
import io.realm.RealmResults

class ViewHolder(
    itemView: View,
    activity: Activity,
    list: RealmResults<Class_Names>
) : RecyclerView.ViewHolder(itemView) {
    val class_name: TextView
    val subject_name: TextView
    val imageView_bg: ImageView
    val frameLayout: RelativeLayout
    val cardView: MaterialCardView
    val total_students: TextView

    init {
        class_name = itemView.findViewById(R.id.className_adapter)
        subject_name = itemView.findViewById(R.id.subjectName_adapter)
        imageView_bg = itemView.findViewById(R.id.imageClass_adapter)
        frameLayout = itemView.findViewById(R.id.frame_bg)
        cardView = itemView.findViewById(R.id.cardView_adapter)
        total_students = itemView.findViewById(R.id.totalStudents_adapter)
        itemView.setOnClickListener { view ->
            val intent = Intent(view.context, ClassDetailActivity::class.java)
            intent.putExtra("theme", list[adapterPosition]!!.position_bg)
            intent.putExtra("className", list[adapterPosition]!!.name_class)
            intent.putExtra("subjectName", list[adapterPosition]!!.name_subject)
            intent.putExtra("classroom_ID", list[adapterPosition]!!.id)
            val p1 = Pair.create(cardView as View, "ExampleTransition")
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1)
            view.context.startActivity(intent, optionsCompat.toBundle())
        }
    }
}