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

    public ArrayList populateHair(){

        hairList.add(R.drawable.hair1);
        hairList.add(R.drawable.hair2);

        return hairList;

    }
    public ArrayList populateHead() {
        headList.add(R.drawable.head1);
        headList.add(R.drawable.head2);
        return headList;
    }

    public ArrayList populateBody() {
        bodyList.add(R.drawable.body1);
        bodyList.add(R.drawable.body2);
        return bodyList;
    }



}

