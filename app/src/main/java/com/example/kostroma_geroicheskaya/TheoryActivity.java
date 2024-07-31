package com.example.kostroma_geroicheskaya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Timer;
import java.util.TimerTask;

public class TheoryActivity extends BaseActivity {
    ImageView image;
    Timer HeroTimer;
    int screenWidth, screenHeight;
    Button back, forward, continueButton;
    TextView dialogWindow;
    String serialisetext;
    LinearLayout controlDialog;
    DialogControl control = new DialogControl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);
        setStatusBarColor(R.color.darkGreen);
        Button backToStation = findViewById(R.id.Back);
        backToStation.setOnClickListener(v -> {
            Intent intent = new Intent(TheoryActivity.this, StationMenuActivity.class);
            startActivity(intent);
        });
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TheoryActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
        image = findViewById(R.id.Image);
//        hero = findViewById(R.id.Hero);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
//        hero.setX(screenWidth*1.25f);
//        setSize(hero, 300, 750);
//        HeroTimer = new Timer();
//        HeroTimer.schedule(new TimerTask() {
//            public void run() {
//                HeroTimer();
//            }
//        }, 0, 10);
        dialogWindow = findViewById(R.id.TextWindow);
        back = findViewById(R.id.TextBack);
        back.setEnabled(false);
        forward = findViewById(R.id.TextForward);
        controlDialog = findViewById(R.id.ControlDialog);
        continueButton = findViewById(R.id.LocationButton);
        continueButton.setVisibility(View.INVISIBLE);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkSerializedKeyExistence("station", "theory"+serialisetext, TheoryActivity.this)) {
                    serializeText("station", "station", "theory"+serialisetext);
                    Integer temp = Integer.parseInt(deserializeText("progress_rewards", "progress_rewards", TheoryActivity.this))+5;
                    serializeText(String.valueOf(temp), "progress_rewards", "progress_rewards");
                }
                Intent intent = new Intent(TheoryActivity.this, StationMenuActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.back();
                changeColor(back, control.getBackEnable());
                changeColor(forward, control.getForwardEnable());
                back.setEnabled(control.getBackEnable());
                forward.setEnabled(control.getForwardEnable());
                dialogWindow.setText(control.getText());
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.forward();
                changeColor(back, control.getBackEnable());
                changeColor(forward, control.getForwardEnable());
                back.setEnabled(control.getBackEnable());
                forward.setEnabled(control.getForwardEnable());
                dialogWindow.setText(control.getText());
                if (control.getI() >= control.getDialogSize())
                    continueButton.setVisibility(View.VISIBLE);
            }
        });


        TextView title = findViewById(R.id.Title);
        ImageView imageView = findViewById(R.id.Image);
        try {
            serialisetext = deserializeText("serIndexStation", "index", TheoryActivity.this);
            int temp = Integer.parseInt(deserializeText("serIndexStation", "serIndexStation"+serialisetext, TheoryActivity.this));
            switch (temp) {
                case 0: {
                    control.addToList("Мемориал «Жителям Пантусово — участникам Великой Отечественной войны 1941–1945 годов» в Костроме появился по инициативе ТОСа микрорайона и неравнодушных жителей.");
                    control.addToList("Памятный знак установлен у дома №1 по второму Пантусовскому проезду. Раньше на этом месте находилась одноименная деревня.");
                    control.addToList("Из этих мест на фронт ушли 125 солдат. Установить все имена помогли работники областного военкомата.");
                    dialogWindow.setText(control.getText());
                    title.setText("Жителям Пантусово");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.pantusino));
                    break;
                }
                case 1: {
                    control.addToList("Евгений Львович Ермаков родился 5 марта 1953 года в городе Правдинске Калининградской области. Детство и юность провёл в городе Костроме, окончил там среднюю школу № 7.");
                    control.addToList("В 1971—1973 годах проходил срочную службу в Краснознамённом Кремлёвском полку специального назначения.");
                    control.addToList("В марте 1982 года Ермаков был зачислен в состав отряда специального назначения Министерства внутренних дел СССР «Кобальт», предназначенного для отправки в Демократическую Республику Афганистан.");
                    control.addToList("Принимал участие в целом ряде специальных операций. 11 августа 1982 года в районе города Кандагара подразделение Ермакова попало в окружение превосходящих сил моджахедов.");
                    control.addToList("Приняв командование на себя вместо погибшего офицера, Ермаков сумел организовать прорыв из вражеского кольца, а затем остался прикрывать отход солдат. В том бою он погиб, так и не дождавшись подкрепления.");
                    control.addToList("Указом Президиума Верховного Совета СССР от 27 января 1983 года капитан внутренней службы Евгений Львович Ермаков посмертно был удостоен ордена Красного Знамени.");
                    control.addToList("В 2013 году Ермаков также посмертно был удостоен звания «Почётный гражданин города Костромы».");
                    dialogWindow.setText(control.getText());
                    title.setText("Ермаков Е.Л.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.ermakov));
                    break;
                }
                case 2: {
                    control.addToList("Алексей Константинович Голубков — сержант Рабоче-крестьянской Красной Армии, участник Великой Отечественной войны, Герой Советского Союза (1945).");
                    control.addToList("В августе 1942 года добился своей отправки на фронт. Первоначально был красноармейцем-связистом.");
                    control.addToList("Затем стал командиром отделения связи 923-го артиллерийского полка 357-й стрелковой дивизии 43-й армии 1-го Прибалтийского фронта.");
                    control.addToList("6 июля 1944 года на подступах к Швенчёнису немецкие войска предприняли попытку остановить наступавшие советские части.");
                    control.addToList("Голубков обеспечивал непрерывную связь между батареями, в течение двух часов под массированным вражеским огнём восстанавливая провода. Три раза был ранен, но своего поста не покинул.");
                    control.addToList("Во время боя Голубков лично уничтожил 18 солдат и офицеров противника. В одном из помещений костёла рядом с Голубковым разорвалась граната. От полученных ранений он скончался на месте.");
                    control.addToList("Указом Президиума Верховного Совета СССР от 24 марта 1945 года за «образцовое выполнение боевых заданий командования на фронте борьбы с немецкими захватчиками и проявленные при этом отвагу и геройство».");
                    control.addToList("сержант Алексей Голубков посмертно был удостоен высокого звания Героя Советского Союза. Также был награждён орденом Ленина и рядом медалей.");
                    dialogWindow.setText(control.getText());
                    title.setText("Голубков А.К.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.golubkov));
                    break;
                }
                case 3: {
                    control.addToList("Борис Михайлович Победимский (1926–2014) — подполковник, участник Великой Отечественной войны, военный дирижёр и музыкант, заслуженный деятель искусств РСФСР.");
                    control.addToList("Родился в Костроме в семье служащих. Обучался в 26-й средней школе города. В 1932 году поступил в Костромскую музыкальную школу по классу фортепиано, которую с отличием окончил в 1940 году.");
                    control.addToList("В годы войны проходил службу в военном оркестре 3-го Ленинградского артиллерийского училища, находившегося в эвакуации в Костроме.");
                    control.addToList("В 1943 году являлся музыкальным руководителем концертно-эстрадной бригады Костромского гарнизонного Дома офицеров по обеспечению прифронтовых районов.");
                    control.addToList("Окончил консерваторию с отличием по двум отделениям, как военный дирижёр и пианист. С 1961 года Победимский служил в ансамбле песни и пляски Северного флота.");
                    control.addToList("Сначала занимал должность заместителя начальника ансамбля, а затем (1963–1985) являлся художественным руководителем этого коллектива.");
                    control.addToList("После выхода на пенсию Победимский вернулся на свою малую родину в Кострому.");
                    control.addToList("В последние годы жизни он активно занимался общественной работой, являлся членом Морского собрания, выступал перед молодёжью. Награждён орденом «Знак почёта», медалью «За победу над Германией»");
                    dialogWindow.setText(control.getText());
                    title.setText("Победимский Б.М.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.pobeda));
                    break;
                }
                case 4: {
                    control.addToList("Юрий Беленогов — младший лейтенант Рабоче-крестьянской Красной Армии, участник Великой Отечественной войны, Герой Советского Союза (1944).");
                    control.addToList("Родился 10 июня 1923 года в селе Селище (ныне в черте Костромы) в рабочей семье. ");
                    control.addToList("После окончания неполной средней школы и школы фабрично-заводского ученичества работал вместе с отцом на заводе «Рабочий металлист».");
                    control.addToList("В 1942 году был призван на службу в армию и направлен на учёбу в Пушкинское танковое училище, эвакуированное в Рыбинск.");
                    control.addToList("С июня 1943 года — на фронтах Великой Отечественной войны, был командиром танка 119-го танкового полка 10-й гвардейской армии Западного фронта.");
                    control.addToList("Покинув подбитый танк, экипаж принял бой, уничтожив 27 вражеских солдат и офицеров (16 из них уничтожил Беленогов). Весь экипаж танка погиб. Оставшись один, Беленогов подорвал себя и окруживших его немецких солдат гранатой.");
                    control.addToList("Указом Президиума Верховного Совета СССР от 3 июня 1944 года за «образцовое выполнение боевых заданий командования на фронте борьбы с немецко-фашистским захватчиками и проявленные при этом мужество и героизм»...");
                    control.addToList("...младший лейтенант Юрий Беленогов посмертно был удостоен высокого звания Героя Советского Союза.");
                    dialogWindow.setText(control.getText());
                    title.setText("Беленогов Ю.С.");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.belenogov));
                    break;
                }
                case 5: {
                    control.addToList("В правобережном районе Костромы, вдали от популярных туристических маршрутов, в тени деревьев небольшого сквера на пересечении Ярославской и Строительной улиц...");
                    control.addToList("...со стороны фасада дома культуры завода «Рабочий металлист» был установлен памятник танку модели Т-34-85.");
                    control.addToList("Танки Т-34-85 стоят на постаментах во многих городах России. Наверное, это самый распространённый танк-памятник в нашей стране.");
                    control.addToList("Причём стоят памятники «тридцатьчетвёрки» не только в городах, находившихся в зоне боевых действий, но и в глубоком тылу.");
                    control.addToList("В годы Великой Отечественной войны Кострома была тыловым городом, тем не менее вклад костромичей в Победу был огромным. В городе работали предприятия по выпуску боеприпасов, деталей стрелкового оружия.");
                    control.addToList("Судомеханический завод делал катера-тральщики, предприятия лёгкой промышленности давали фронту обмундирование, плащ-палатки, авиаполотно. А колхозники обеспечивали продовольствием и фронтовиков, и тыловиков.");
                    control.addToList("В 1980 году решили поставить на перекрестке улиц Магистральной и Голубкова танк на высоком постаменте. Идея принадлежала Виталию Федотовичу Широкову — он бывший танкист.");
                    control.addToList("Почему же танк установили в Костроме, если через наш город фронт не проходил?");
                    control.addToList("Танк установили в честь костромичей, на средства которых в войну построили технику для двух танковых дивизий.");
                    control.addToList("Колхозники собирали деньги на строительство танковых и авиационных колонн.");
                    control.addToList("Костромичами было собрано более 70 миллионов рублей, на которые была построена танковая колонна «Иван Сусанин».");
                    control.addToList("В итоге, мощный постамент на ул. Магистральной был разобран и памятник Т-34-85 установили на пересечении Ярославской и Строительной улиц.");
                    control.addToList("Перед танком расположен камень с табличкой «Доблестным защитникам отечества, труженикам тыла, победителям в Великой Отечественной войне 1941-1945 г.г.»");
                    control.addToList("На задней стенке литой башни отчётливо видны цифры в два ряда «2672 3». Вообще, внешний вид танка позволяет предположить, что сделан он на 183-м заводе в Нижнем Тагиле.");
                    dialogWindow.setText(control.getText());
                    title.setText("Памятник Т-34-85");
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.tank));
                    break;
                }
            }
        }
        catch (Exception e) {}
    }

    private void setTextColor(Button textView, Integer color) {
        String hexColor = String.format("#%06X", ContextCompat.getColor(this, color) & 0xFFFFFF);
        textView.setBackgroundColor(Color.parseColor(hexColor));
    }

    private void changeColor(Button button,boolean b) {
        if (b) {
            setTextColor(button, R.color.darkGreen);
        }
        else {
            setTextColor(button, R.color.disable);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TheoryActivity.this, StationMenuActivity.class);
        startActivity(intent);
    }

//    private void HeroTimer() {
//        this.runOnUiThread(doJump);
//    }
//
//    private Runnable doJump = new Runnable() {
//        public void run()
//        {
//            if (hero.getX()>= screenWidth-hero.getWidth()) {
//                hero.setX(hero.getX() - 8);
//                hero.setY((screenHeight-hero.getHeight()*2)/2);
//            }
//            else   {
//                if (HeroTimer != null) {
//                    HeroTimer.cancel();
//                    HeroTimer = null;
//                    back.setVisibility(View.VISIBLE);
//                    forward.setVisibility(View.VISIBLE);
//                    dialogWindow.setVisibility(View.VISIBLE);
//                    controlDialog.setY(image.getY()+image.getHeight()+50);
//                    continueButton.setY(controlDialog.getY()+controlDialog.getHeight()+50);
//                    dialogWindow.setText(control.getText());
//                    back.setEnabled(control.getBackEnable());
//                    forward.setEnabled(control.getForwardEnable());
//                }
//            }
//        }
//    };
//
//    private void setSize(View view, int x, int y) {
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        params.width = x;
//        params.height = y;
//        view.setLayoutParams(params);
//    }
}