
package jp.ac.ritsumei.ise.phy.exp2.is0429rs.medicinecalculator

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.activity_medicine_regist.*


class MedicineRegistActivity : AppCompatActivity() {

    private lateinit var helper: MedicineDataOpenHelper ;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_regist)

        helper = MedicineDataOpenHelper(applicationContext);    //DB作成

        /*上段モード切り替え*/
        val radioGroup_modeChange : RadioGroup = findViewById(R.id.ModeChange)
        radioGroup_modeChange.setOnCheckedChangeListener  { _, checkedId : Int ->
            if (checkedId == R.id.medCul) {
                //薬計算画面への遷移
                val intent_medCul : Intent = Intent(this, MainActivity::class.java);
                startActivity(intent_medCul)

            } else if (checkedId == R.id.accounting) {
                //会計画面への遷移
                val intent_accounting : Intent = Intent(this, AccountingActivity::class.java)
                startActivity(intent_accounting)

            } else if (checkedId == R.id.history) {
                //履歴画面への遷移
                val intent_history: Intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent_history)
            }
        }


        /*薬の既存変更に関する設定*/
        /*have not written yet. ----------------------------------------------------------------------------

       ---------------------------------------------------------------------------------------------------*/
    }

    /* DBからデータをすべて取得し薬の名前を配列にして返す*/
    fun medicineNameData(): ArrayList<String> {
        val db: SQLiteDatabase = helper.readableDatabase;
        val cursor: Cursor = db.query(
            "testdb",
            arrayOf("medicine", "value", "kind"),
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val medicineNameList = arrayListOf<String>();

        for (i in 1..cursor.count) {
            medicineNameList.add(cursor.getString(0))
            cursor.moveToNext()
        }

        cursor.close()

        return medicineNameList
    }

    /* DBからデータをすべて取得し薬の値段を配列にして返す*/
    fun medicineValueData(): ArrayList<Int> {
        val db: SQLiteDatabase = helper.readableDatabase;
        val cursor: Cursor = db.query(
            "testdb",
            arrayOf("medicine", "value", "kind"),
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val medicineValueList = arrayListOf<Int>();

        for (i in 1..cursor.count) {
            medicineValueList.add(cursor.getInt(1))
            cursor.moveToNext()
        }

        cursor.close()

        return medicineValueList
    }

    /* DBからデータをすべて取得し薬の種類を配列にして返す*/
    fun medicineKindData(): ArrayList<String> {
        val db: SQLiteDatabase = helper.readableDatabase;
        val cursor: Cursor = db.query(
            "testdb",
            arrayOf("medicine", "value", "kind"),
            null,
            null,
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val medicineKindList = arrayListOf<String>();

        for (i in 1..cursor.count) {
            medicineKindList.add(cursor.getString(2))
            cursor.moveToNext()
        }

        cursor.close()

        return medicineKindList
    }

    /**
     *データを保存する
     * @param view
     * */
    fun saveData(view: View) {
        val db: SQLiteDatabase = helper.writableDatabase;
        val values: ContentValues = ContentValues();

        //薬の名前と値段を取得
        val medNameValue = findViewById<EditText>(R.id.medNameValue)  as EditText   //薬の名前を受け取る
        val medValue = findViewById<EditText>(R.id.simpleValue) as EditText //薬の値段を受け取る
        //薬の種類を取得・ラジオボタンに関する変数
        val radioGroup_medKind : RadioGroup = findViewById(R.id.medKindRadioGroup)  //薬の種類のラジオボタン
        val radioId = radioGroup_medKind.checkedRadioButtonId;
        val medKindValue: RadioButton = radioGroup_medKind.findViewById<RadioButton>(radioId);
        //val index = radioGroup.indexOfChild(radioButton);

        //表示させる形式に変数を変換
        val medicine: String = medNameValue.text.toString();
        val value: Int = medValue.text.toString().toInt();
        val kind: String = medKindValue.text.toString();

        values.put("medicine", medicine)
        values.put("value", value)
        values.put("kind", kind)

        db.insertOrThrow("testdb", null, values)
    }

    /*/*登録ボタンが押されたときに呼ばれる関数*/
    fun registButton_isTriger() {
        //薬の名前と値段を取得
        val medNameValue = findViewById<EditText>(R.id.medNameValue)  as EditText   //薬の名前を受け取る
        val medValue = findViewById<EditText>(R.id.simpleValue) as EditText //薬の値段を受け取る
        //薬の種類を取得・ラジオボタンに関する変数
        val radioGroup_medKind : RadioGroup = findViewById(R.id.medKindRadioGroup)  //薬の種類のラジオボタン
        val radioId = radioGroup_medKind.checkedRadioButtonId;
        val medKindValue: RadioButton = radioGroup_medKind.findViewById<RadioButton>(radioId);

        //登録ボタンが押されたときの処理
        regist.setOnClickListener {
            //EditTextのテキストの取得
            if (medNameValue.text != null && medValue.text != null && medKindValue != null) {   //テキストが入力された状態であるとき
                saveData(regist)
                medNameValue.getEditableText().clear()      //EditTextを空にする
                medValue.getEditableText().clear()
                radioGroup_medKind.clearCheck()     //ラジオボタンの選択を外す
            } else {
                if (medNameValue.text == null) {
                    medNameValue.setError("名前を入力してください")
                }
                if (medValue.text == null) {
                    medValue.setError("単価を入力してください")
                }
                if (medKindValue == null) {
                }
            }
        }
    }*/

    /*薬の種類を返す関数*/
    fun medKindText() : String {
        val radioGroup_medKind : RadioGroup = findViewById(R.id.medKindRadioGroup)
        var medKindName = ""
        radioGroup_medKind.setOnCheckedChangeListener {_, checkedId : Int ->
            if (checkedId == R.id.tablet) {
                medKindName = "tablet"
            } else if (checkedId == R.id.powder) {
                medKindName = "powder"
            } else if (checkedId == R.id.liquid) {
                medKindName = "liquid"
            }
        }
        return medKindName
    }


}




