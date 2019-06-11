package jp.ac.ritsumei.ise.phy.exp2.is0429rs.medicinecalculator

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.*
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var helper: MedicineDataOpenHelper ;

    var mValues = arrayOfNulls<Double>(20); //薬ごとに計算し終えた値を保存しておく配列
    var mValuesUpdate : Int = 0;    //mValuesの位置を移動させるための数
    var mValueTime : Double? = 0.0;  //一時的に薬の計算を行うための数
    var day : Int = 0;  //日数
    var lastValue : Double? = 0.0;   //合計の値

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helper = MedicineDataOpenHelper(applicationContext);    //DB作成

        /*上段モード切り替え*/
        val radioGroup_modeChange: RadioGroup = findViewById(R.id.ModeChange)
        radioGroup_modeChange.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.medList) {
                //薬登録画面への遷移
                val intent_medReg: Intent = Intent(this, MedicineRegistActivity::class.java)
                startActivity(intent_medReg)

            } else if (checkedId == R.id.accounting) {
                //会計画面への遷移
                val intent_accounting: Intent = Intent(this, AccountingActivity::class.java)
                startActivity(intent_accounting)

            } else if (checkedId == R.id.history) {
                //履歴画面への遷移
                val intent_history: Intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent_history)
            }
        }

        
        /*スピナーの設定*/
        val spinner: Spinner = findViewById(R.id.spinner)
        if (medicineNameData() != null) {
            setAdapter(spinner, medicineNameData())
        } else {
            /*登録されているデータがない場合の処理-----------------

            ------------------------------------------------------*/
        }

        /*日数に関する設定*/
        SID.setOnClickListener {
            mValueTime = mValueTime!! * 1;
        }

        BID.setOnClickListener {
            mValueTime = mValueTime!! * 2;
        }

        QID.setOnClickListener {
            if(day%2 == 0) {
                mValueTime = mValueTime!! * 1/2;
            } else {
                day += 1;
                mValueTime = mValueTime!! * 1/2;
            }
        }

        TID.setOnClickListener {
            if (day%3 == 0) {
                mValueTime = mValueTime!! * 1/3;
            }else if(day%3 == 1) {
                day += 2;
                mValueTime = mValueTime!! * 1/3;
            } else {
                day += 1;
                mValueTime = mValueTime!! * 1/3;
            }

        }

        /*錠数に関する設定*/
        oeT.setOnClickListener {
            mValueTime = mValueTime!! * 1/8;
        }

        ofT.setOnClickListener {
            mValueTime = mValueTime!! * 1/4;
        }

        otT.setOnClickListener {
            mValueTime = mValueTime!! * 1/2;
        }

        oneT.setOnClickListener {
            mValueTime = mValueTime!! * 1;
        }

        twoT.setOnClickListener {
            mValueTime = mValueTime!! * 2;
        }

        threeT.setOnClickListener {
            mValueTime = mValueTime!! * 3;
        }

        /*日数に関する設定*/
        val editText = findViewById<EditText>(R.id.daysValue);
        val str : Editable = editText.getText()

        decision.setOnClickListener {
            day = day + Integer.parseInt(str.toString());
            mValueTime = mValueTime!! * day;
        }

        /*演算に関する設定*/
        delete.setOnClickListener {
            mValueTime = 0.0;
        }

        allclear.setOnClickListener {
            for (i in 0..mValues.size) {
                mValues[i] = 0.0;
            }
        }

        add.setOnClickListener {
            mValues[mValuesUpdate] = mValueTime;    //計算後の値を配列に格納
            val messageView : TextView = findViewById(R.id.unitvalue)
            messageView.text = mValueTime.toString()

            mValuesUpdate += 1; //配列格納後にポインタを1ずらす
            mValueTime = 0.0;   //薬の値段を0にリセットする
        }

        equal.setOnClickListener {
            lastValue.sum(mValues)
            val messageView2 : TextView = findViewById(R.id.addvalue)
            messageView2.text = lastValue.toString()
        }
    }

    /* DBからデータをすべて取得し薬のデータを配列にして返す*/
    fun medicineData(): ArrayList<MedicineAllData> {
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

        val medicineList = arrayListOf<MedicineAllData>();

        for (i in 1..cursor.count) {
            medicineList.add(MedicineAllData(cursor.getString(0), cursor.getInt(1), cursor.getString(2)))
            cursor.moveToNext()
        }

        cursor.close()

        return medicineList
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


    /*spinnerにString型のArrayListをセットする関数*/
    private fun setAdapter(spinner: Spinner, list: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter;
    }
}


private fun Double?.sum(values: kotlin.Array<Double?>) : Double {
    var result  = 0.0;
    for(i in 0..19) {

        if (!values.isEmpty()) {
            var number : Double? = values[i]
            if(number != null) {
                result += number
            }
        }
        //result += values[i]?.let { it1 -> result?.plus(it1) }!!

    }
    return result
}

class MedicineAllData(val medicineName: String, val medicineValue: Int, val medicineKind: String)





