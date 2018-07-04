package com.example.nttr.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // ① 変数の宣言
    TextView countTextView;
    TextView contentTextView;
    // Buttonの配列
    // ボタンを3つセットで管理したいので、配列で宣言します。
    Button[] optionButtons;
    // 現在の問題番号
    int questionNumber;
    // 獲得したポイント数
    int point;
    // Listという、配列よりもデータを追加しやすい型を使います。
    List<Quiz> quizList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ② 初期化処理
        // Viewの関連付けを行います
        countTextView = findViewById(R.id.txtQuizNum);
        contentTextView = findViewById(R.id.txtQuestion);
        // 配列を初期化します
        // 今回、ボタンは3つなので、3の大きさの配列を用意します
        optionButtons = new Button[3];
        optionButtons[0] = findViewById(R.id.button1);
        optionButtons[1] = findViewById(R.id.button2);
        optionButtons[2] = findViewById(R.id.button3);
        for(Button button : optionButtons) {
            // レイアウトではなくコードからonClickを設定します
            // setOnClickListenerに、thisを入れて呼び出します
            button.setOnClickListener(this);
        }

        // Quizを1から始めるのでリセットする
        resetQuiz();
    }

    // ③ 出題するクイズのリストを作成します
    void createQuizList() {
        Quiz quiz1 = new Quiz("ドラえもんの身長は何センチ？", "127.3cm", "128.3cm", "129.3cm", "129.3cm");
        Quiz quiz2 = new Quiz("ドラえもんの嫌いな生き物は？", "猫", "タヌキ", "ネズミ", "ネズミ");
        Quiz quiz3 = new Quiz("ドラえもんの好きな食べ物は？", "ラーメン", "牛丼", "どら焼き", "どら焼き");
        Quiz quiz4 = new Quiz("ドラえもんは昔何色だった？", "黄色", "赤色", "水色", "黄色");
        Quiz quiz5 = new Quiz("アニメ版ドラえもん、「ドラえもんのうた」に出てこない道具は？", "タケコプター", "スモールライト", "どこでもドア", "スモールライト");
        Quiz quiz6 = new Quiz("今何問目？","","","","");
        // Listを実装したArrayListというものを使います
        quizList = new ArrayList<>();
        quizList.add(quiz1);
        quizList.add(quiz2);
        quizList.add(quiz3);
        quizList.add(quiz4);
        quizList.add(quiz5);
        quizList.add(quiz6);
        // Listの中身をシャッフルします
        Collections.shuffle(quizList);

        // 今何問目という問題の内容を確定
        // 課題171207：乱数作って挿入した方が早い？
        for (int i = 0; i < quizList.size(); i++) {
            Quiz qq1 = quizList.get(i);
            String strAnswer = qq1.answer;
            // 見つけたら更新して、ループ抜ける
            if (strAnswer.equals("")) {
                Quiz qq2 = new Quiz("今何問目？", (i) +"問目",(i+1) +"問目",(i+2) +"問目",(i+1) +"問目");
                quizList.set(i,qq2);
                break;
            }
        } //for i

    }

    // ④ クイズを表示します
    void showQuiz() {
        // 表示する問題をリストから取得します。
        // 配列では[番号]でしたが、リストでは get(番号)で取得します。配列と同じく0からスタートです。
        Quiz quiz = quizList.get(questionNumber);
        contentTextView.setText(quiz.content);
        optionButtons[0].setText(quiz.option1);
        optionButtons[1].setText(quiz.option2);
        optionButtons[2].setText(quiz.option3);

        // 問題番号を隠す場合
        if (quiz.content.equals("今何問目？")) {
            countTextView.setText("？問目");
        } else {
            countTextView.setText((questionNumber + 1) + "問目");
        }
    }

    // ⑤ クイズのリセット
    void resetQuiz() {
        questionNumber = 0;
        point = 0;
        createQuizList();
        showQuiz();
    }

    // ⑥ クイズのアップデート
    void updateQuiz() {
        questionNumber++;
        // 問題全部　→　定数使えば一部にできそう
        if (questionNumber < quizList.size()) {
            showQuiz();
        } else {
            // 全ての問題を解いてしまったので、結果を表示します。
            // 結果表示にはDialogを使います。
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("結果");
            builder.setMessage(quizList.size() + "問中、" + point + "問正解でした。");
            builder.setPositiveButton("リトライ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // もう一度クイズをやり直す
                    resetQuiz();
                }
            });
            builder.show();
        }
    }

    // ⑦ setOnClickListerを呼び出したViewをクリックした時に呼び出されるメソッド
    @Override
    public void onClick(View view) {
        // 引数のViewには、押されたViewが入っています。
        // Buttonが押された時しかよばれないので、キャストといって型を示してあげます。
        Button clickedButton = (Button) view;
        Quiz quiz = quizList.get(questionNumber);
        // ボタンの文字と、答えが同じかチェックします。
        if (TextUtils.equals(clickedButton.getText(), quiz.answer)) {
            // 正解の場合だけ1ポイント加算します。
            point++;
            Toast.makeText(this, "正解", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "はずれ", Toast.LENGTH_SHORT).show();
        }
        // 次の問題にアップデートします。
        updateQuiz();
    }

}
