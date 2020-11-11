package pl.translaide

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var inputText:EditText
    lateinit var flagBtn:ImageView
    var LANG:String = "pl"
    val REQ_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.input_text)
        flagBtn = findViewById(R.id.flag)

        flagBtn.setOnClickListener { changeLang() }

        //looks like shit, but works
        //it
        inputText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val DRAWABLE_LEFT = 0
                val DRAWABLE_TOP = 1
                val DRAWABLE_RIGHT = 2
                val DRAWABLE_BOTTOM = 3

                if(event!!.action == MotionEvent.ACTION_UP){
                    if(event!!.getRawX() >= (inputText.right - inputText.compoundDrawables[DRAWABLE_RIGHT].bounds.width())){
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                        intent.putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        if(LANG.equals("pl")){
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pl_PL")
                        }else{
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de_DE")
                        }
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Zacznij mówić")
                        try {
                            startActivityForResult(intent, REQ_CODE)
                        } catch (a: ActivityNotFoundException) {
                            Toast.makeText(
                                applicationContext,
                                "Sorry your device not supported",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return true
                    }
                }
                return false
            }
        })
    }


    private fun changeLang(){
        Log.i("XD","it works!")
        if(LANG.equals("de")){
            LANG = "pl"
            flagBtn.setImageResource(R.drawable.ic_pl)
        }else{
            LANG = "de"
            flagBtn.setImageResource(R.drawable.ic_de)
        }
    }


    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQ_CODE ->{
                if (resultCode == RESULT_OK && null != data) {
                    var result: java.util.ArrayList<String>? = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    inputText.setText(result!!.get(0));
                }
            }
        }
    }

}