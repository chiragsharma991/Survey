package com.softtech360.totalservey.model


data class QuestionAnswer(val data: Data,
                          val message: String = "",
                          val status: Boolean )


data class Data (

        val form_sections: ArrayList<FormSections>,
        val app_version : String?="1.0"
)


data class FormSections(val section_id: Int,
                        val name: String = "",
                        val is_pwd: Int = 0,
                        val questions: ArrayList<Questions>)


data class Questions(val question: String = "",
                     val question_type: Int,
                     val answers: ArrayList<Answers> = arrayListOf(),
                     val question_id: Int)


data class Answers(val answer: String = "",
                   val answer_id: Int = 0,
                   val question_id: Int=0,
                   var is_selected: Boolean = false,
                   var is_values: String = ""

)


