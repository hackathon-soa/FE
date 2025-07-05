package com.example.soa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.soa.data.Applicant
import com.example.soa.databinding.ItemApplicantBinding

class ApplicantAdapter(
    private val items: List<Applicant>
) : RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder>() {

    inner class ApplicantViewHolder(private val binding: ItemApplicantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Applicant) {
            binding.tvName.text = item.name
            binding.tvDisability.text = item.disability
            binding.tvTemperature.text = "${item.temperature}℃"
            binding.tvContact.text = "연락처 | ${item.contact}"
            binding.tvGender.text = "성별 | ${item.gender}"
            binding.tvAge.text = "나이 | ${item.age}"
            binding.tvTime.text = "봉사시간 | ${item.volunteerTime}"
            binding.tvMileage.text = "마일리지 | ${item.mileage}"

            // 버튼 리스너 예시
            binding.btnAccept.setOnClickListener {
                Toast.makeText(binding.root.context, "${item.name} 수락됨", Toast.LENGTH_SHORT).show()
            }
            binding.btnDecline.setOnClickListener {
                Toast.makeText(binding.root.context, "${item.name} 거절됨", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {
        val binding = ItemApplicantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ApplicantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
