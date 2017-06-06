package com.ffb.friendsfootbets;


/**
 * Created by mehdimand on 03/06/2017.
 * https://openclassrooms.com/courses/creez-des-applications-pour-android/tp-un-bloc-notes
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class Slider extends RelativeLayout {
    /* Est-ce que le tohide est ouvert ? */
    protected boolean isOpen = false;
    /* Le tohide à dissimuler */
    protected ListView toHide = null;

    /**
     * Constructeur utilisé pour l'initialisation en Java.
     *
     * @param context Le contexte de l'activité.
     */
    public Slider(Context context) {
        super(context);
    }

    /**
     * Constructeur utilisé pour l'initialisation en XML.
     *
     * @param context Le contexte de l'activité.
     * @param attrs   Les attributs définis dans le XML.
     */
    public Slider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Utilisée pour ouvrir ou fermer le tohide.
     *
     * @return true si le tohide est désormais ouvert.
     */
    public boolean toggle() {

        //On passe de ouvert à fermé (ou vice versa)
        isOpen = !isOpen;

        //Si le tohide est déjà ouvert
        if (isOpen) {
            toHide.setVisibility(View.VISIBLE);
        } else {
            toHide.setVisibility(View.GONE);
        }

        return isOpen;
    }


    public void setToHide(ListView toHide) {
        this.toHide = toHide;
    }
}
