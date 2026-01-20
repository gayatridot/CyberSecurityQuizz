package com.example.cybersecurity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuizAdapter(
    private val questions: List<Question>,
    private val onAnswerSelected: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    private val userAnswers = mutableMapOf<Int, String>()
    private val feedbackShown = mutableMapOf<Int, Boolean>()

    class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText: TextView = itemView.findViewById(R.id.ques)
        val optionsGroup: RadioGroup = itemView.findViewById(R.id.optionsGroup)
        val option1: RadioButton = itemView.findViewById(R.id.option1)
        val option2: RadioButton = itemView.findViewById(R.id.option2)
        val option3: RadioButton = itemView.findViewById(R.id.option3)
        val option4: RadioButton = itemView.findViewById(R.id.option4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemques, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val question = questions[position]
        holder.questionText.text = "${position + 1}. ${question.question}"
        holder.option1.text = question.option1
        holder.option2.text = question.option2
        holder.option3.text = question.option3
        holder.option4.text = question.option4

        // Reset state
        holder.optionsGroup.clearCheck()
        resetOptionColors(holder)
        
        // Disable if already answered to prevent re-selection (per "show result when attempt")
        if (feedbackShown[position] == true) {
            disableOptions(holder)
            highlightResult(holder, question, userAnswers[position])
        } else {
            enableOptions(holder)
        }

        holder.optionsGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1 && feedbackShown[position] != true) {
                val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
                val selectedAnswer = selectedRadioButton.text.toString()
                
                userAnswers[position] = selectedAnswer
                feedbackShown[position] = true
                
                val isCorrect = selectedAnswer == question.answer
                onAnswerSelected(position, isCorrect)
                
                disableOptions(holder)
                highlightResult(holder, question, selectedAnswer)
            }
        }
    }

    private fun highlightResult(holder: QuizViewHolder, question: Question, selectedAnswer: String?) {
        val options = listOf(holder.option1, holder.option2, holder.option3, holder.option4)
        
        options.forEach { option ->
            val optionText = option.text.toString()
            if (optionText == question.answer) {
                option.setBackgroundColor(Color.parseColor("#C8E6C9")) // Green highlight
            } else if (optionText == selectedAnswer && selectedAnswer != question.answer) {
                option.setBackgroundColor(Color.parseColor("#FFCDD2")) // Red highlight
            }
        }
    }

    private fun resetOptionColors(holder: QuizViewHolder) {
        val options = listOf(holder.option1, holder.option2, holder.option3, holder.option4)
        options.forEach { it.setBackgroundColor(Color.TRANSPARENT) }
    }

    private fun disableOptions(holder: QuizViewHolder) {
        for (i in 0 until holder.optionsGroup.childCount) {
            holder.optionsGroup.getChildAt(i).isEnabled = false
        }
    }

    private fun enableOptions(holder: QuizViewHolder) {
        for (i in 0 until holder.optionsGroup.childCount) {
            holder.optionsGroup.getChildAt(i).isEnabled = true
        }
    }

    override fun getItemCount(): Int = questions.size
}