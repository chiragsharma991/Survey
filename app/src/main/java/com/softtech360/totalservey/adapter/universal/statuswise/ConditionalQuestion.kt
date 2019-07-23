package com.softtech360.totalservey.adapter.universal.statuswise

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.softtech360.totalservey.activity.HostActivity
import com.softtech360.totalservey.fragment.SectionWise
import org.jetbrains.anko.runOnUiThread
import java.util.ArrayList
import kotlin.concurrent.thread

object ConditionalQuestion{


    fun <T> conditionalIs(model: SectionWise.SectionWiseModel, questionContainer: LinearLayout, list: ArrayList<SectionWise.SectionWiseModel>, c: Context, sectionwise: T) {


        thread {

            for (value in HostActivity.conditionalQuestion) {

                if (value.question_id == model.question_id) {
                    //14==14
                    val target_question = value.conditional_target.target_question_id
                    // loop to get operation in position
                    for (result in list) {
                        // if is target question is present in same section id.
                        if (result.question_id == target_question) {
                            // check condition using target question:
                            when (model.question_id) {

                                14 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // father- mother is selected so
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true


                                        } else {
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        }
                                    }

                                }

                                23 -> {
                                    // radio: की क्या मनोचिकित्सक से दवा चल र
                                    c.runOnUiThread {
                                        if (result.answer[17].is_selected ) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true
                                        }
                                    }

                                }
                                25 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        } else {
                                            // no
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false
                                        }
                                    }

                                }


                                26 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }


                                27 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }

                                32 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }

                                35 -> {
                                    // radio button :
                                    c.runOnUiThread {
                                        Log.e("any toilet--",""+HostActivity.any_toilet+" "+result.answer[1].is_selected )
                                        if (result.answer[1].is_selected && HostActivity.any_toilet) {
                                            // No && yes
                                            // if toilet is presnet and  he use the toilet then show
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false
                                        }else{
                                            // Not show
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }

                                    }

                                }

                                44 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[5].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        } else {
                                            // no
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false
                                        }
                                    }

                                }
                                46 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false
                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true
                                        }
                                    }

                                }


                                48 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }

                                49 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }

                                50 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }

                                59 -> {
                                    // radio button
                                    c.runOnUiThread {
                                        if (result.answer[5].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        } else {
                                            // no
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        }
                                    }

                                }

                                74 -> {
                                    // radio button
                                    c.runOnUiThread {


                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false


                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }

                                    }

                                }
                                75 -> {
                                    // radio button

                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }
                                76 -> {
                                    // radio button

                                    c.runOnUiThread {
                                        if (result.answer[0].is_selected) {
                                            // yes
                                            questionContainer.visibility = View.VISIBLE
                                            model.view_hide_is=false

                                        } else {
                                            // no
                                            questionContainer.visibility = View.GONE
                                            model.view_hide_is=true

                                        }
                                    }

                                }





                            }

                        }
                        else if(model.question_id ==40){
                            // if is target question is not present in same section id.
                                // radio button
                                c.runOnUiThread {
                                    if (HostActivity.statusofstudy == 0) {
                                        // yes
                                        questionContainer.visibility = View.VISIBLE
                                        model.view_hide_is=false

                                    } else {
                                        // no
                                        questionContainer.visibility = View.GONE
                                        model.view_hide_is=true
                                    }


                            }
                        }
                    }


                    break

                }else{

                  /*  c.runOnUiThread {
                        questionContainer.visibility = View.VISIBLE
                        model.view_hide_is=false
                    }*/


                }
            }
        }

    }



}