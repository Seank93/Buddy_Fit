package ie.seankehoe.buddyfit;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Sean Kehoe on 14/04/2017.
 */

public class CharacterStyles {
    DatabaseHelper myDb;

    ArrayList<Integer> hairList = new ArrayList<>();
    ArrayList<Integer> bodyList = new ArrayList<>();
    ArrayList<Integer> headList = new ArrayList<>();
    ArrayList<Integer> enemyList = new ArrayList<>();
    ArrayList<Integer> environList = new ArrayList<>();

    public ArrayList populateHair(){

        hairList.add(R.drawable.hair1);
        hairList.add(R.drawable.hair2);
        hairList.add(R.drawable.hair3);
        hairList.add(R.drawable.hair4);
        hairList.add(R.drawable.hair5);
        return hairList;

    }
    public ArrayList populateHead() {
        headList.add(R.drawable.head1);
        headList.add(R.drawable.head2);
        headList.add(R.drawable.head3);
        headList.add(R.drawable.head4);
        headList.add(R.drawable.head5);
        headList.add(R.drawable.head6);
        return headList;
    }

    public ArrayList populateBody() {
        bodyList.add(R.drawable.body1);
        bodyList.add(R.drawable.body2);
        bodyList.add(R.drawable.body3);
        bodyList.add(R.drawable.body4);
        return bodyList;
    }
    public ArrayList populateEnemies() {
        enemyList.add(R.drawable.enemy1);
        enemyList.add(R.drawable.enemy2);
        enemyList.add(R.drawable.enemy3);
        enemyList.add(R.drawable.enemy4);
        enemyList.add(R.drawable.enemy5);
        enemyList.add(R.drawable.enemy6);
        enemyList.add(R.drawable.enemy7);
        return enemyList;
    }

    public ArrayList populateEnvirons() {
        environList.add(R.drawable.grassbg);
        environList.add(R.drawable.cavebg);
        environList.add(R.drawable.beachbg);
        return environList;

    }
}

