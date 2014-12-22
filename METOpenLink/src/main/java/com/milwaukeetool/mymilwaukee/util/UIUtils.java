package com.milwaukeetool.mymilwaukee.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.milwaukeetool.mymilwaukee.MilwaukeeToolApplication;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.milwaukeetool.mymilwaukee.util.LogUtils.LOGD;
import static com.milwaukeetool.mymilwaukee.util.LogUtils.makeLogTag;

/**
 * Created by cent146 on 10/24/14.
 */
public class UIUtils {

    private static final String TAG = makeLogTag(UIUtils.class);
    /**
     * Regex to search for HTML escape sequences.
     * <p/>
     * <p></p>Searches for any continuous string of characters starting with an ampersand and ending with a
     * semicolon. (Example: &amp;amp;)
     */
    private static final Pattern REGEX_HTML_ESCAPE = Pattern.compile(".*&\\S;.*");

    /**
     * Populate the given {@link android.widget.TextView} with the requested text, formatting
     * through {@link android.text.Html#fromHtml(String)} when applicable. Also sets
     * {@link android.widget.TextView#setMovementMethod} so inline links are handled.
     */
    public static void setTextMaybeHtml(TextView view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setText("");
            return;
        }
        if ((text.contains("<") && text.contains(">")) || REGEX_HTML_ESCAPE.matcher(text).find()) {
            view.setText(Html.fromHtml(text));
            view.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            view.setText(text);
        }
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean hasJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static void showBasicAlertbox(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        //TextView messageTextView = (TextView) alert.findViewById(android.R.id.message);
        //TextView titleTextView = (TextView)alert.findViewById(android.R.id.title);
        //StringUtils.updateFontForTextView(messageTextView);
        //StringUtils.updateFontForTextView(titleTextView);
        alert.show();
    }

    /**
     * Recursively find all child views within the View passed in.
     *
     * @param v
     */
    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();
        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    public static void updateActionBarTitle(Activity activity, String title)
    {
        SpannableString titleString = new SpannableString(title);

        titleString.setSpan(new com.milwaukeetool.mymilwaukee.util.TypefaceSpan(activity, "Arial.ttf"), 0, titleString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        activity.getActionBar().setTitle(titleString);
    }

    public static void showView(View view) {
        UIUtils.hideShowView(view, false);
    }

    public static void hideView(View view) {
        UIUtils.hideShowView(view, true);
    }

    public static void showView(View view, int durationInMilliseconds) {
        UIUtils.hideShowView(view, false, durationInMilliseconds, 0);
    }

    public static void hideView(View view, int durationInMilliseconds) {
        UIUtils.hideShowView(view, true, durationInMilliseconds, 0);
    }

    public static void showView(View view, int durationInMilliseconds, int delay) {
        UIUtils.hideShowView(view, false, durationInMilliseconds, delay);
    }

    public static void hideView(View view, int durationInMilliseconds, int delay) {
        UIUtils.hideShowView(view, true, durationInMilliseconds, delay);
    }

    private static void hideShowView(final View view, final boolean hide, int durationInMilliseconds, int delay) {
        if (UIUtils.canAnimate()) {
            view.animate().setDuration(durationInMilliseconds).setStartDelay(delay).alpha(hide ? 0.0f : 1.0f).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (!hide) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (hide) {
                        view.setVisibility(View.INVISIBLE);
                    }
                }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            }).start();
        } else {
//            AnimatorSet set = new AnimatorSet();
//            set.playTogether(
//                    ObjectAnimator.ofFloat(view, "alpha", 1, (hide ? 0.0f : 1.0f))
//            );
//            set.addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {
//                    if (!hide) {
//                        view.setVisibility(View.VISIBLE);
//                    }
//                }
//                @Override
//                public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
//                    if (hide) {
//                        view.setVisibility(View.INVISIBLE);
//                    }
//                }
//                @Override
//                public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {}
//                @Override
//                public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {}
//            });
//            set.setStartDelay(delay);
//            set.setDuration(durationInMilliseconds).start();
        }
    }

    private static void hideShowView(View view, boolean hide) {
        if (hasHoneycomb()) {
            view.setAlpha(hide ? 0.0f : 1.0f);
        }
        view.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
    }

    public static boolean isViewVisible(View view) {

        if (view == null)
            return false;

        boolean isVisible = true;
        if (hasHoneycomb()) {
            isVisible &= (view.getAlpha() > 0.0);
        }
        isVisible &= (view.getVisibility() == View.VISIBLE);

        return isVisible;
    }

    public static boolean canAnimate() {
        return hasHoneycombMR1();
    }

    public static void updatePositionForView(View view, int duration, int topMargin, int leftMargin) {
        if (UIUtils.canAnimate()) {
            view.animate().setDuration(duration).x(leftMargin).y(topMargin).start();
        } else {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "x", 1, leftMargin),
                    ObjectAnimator.ofFloat(view, "y", 1, topMargin)
            );
            set.setDuration(duration).start();
        }
    }

    public static void updateXPositionForView(View view, int duration, int newXValue) {
        if (canAnimate()) {
            view.animate().setDuration(duration).x(newXValue).start();
        } else {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "x", 1, newXValue)
            );
            set.setDuration(duration).start();
        }
    }

    public static void updateYPositionForView(View view, int duration, int newYValue) {
        if (canAnimate()) {
            view.animate().setDuration(duration).y(newYValue).start();
        } else {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "y", 1, newYValue)
            );
            set.setDuration(duration).start();
        }
    }

    public static void updateScaleForView(View view, int duration, float scale) {
        if (canAnimate()) {
            view.animate().setDuration(duration).scaleX(scale).scaleY(scale).start();
        } else {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(view, "scaleX", 1, scale),
                    ObjectAnimator.ofFloat(view, "scaleY", 1, scale)
            );
            set.setDuration(duration).start();
        }
    }

    public static int getPixels(int dipValue, Context context) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue,
                r.getDisplayMetrics());
        return px;
    }

    public static int getPixels(int dipValue) {
        return getPixels(dipValue, MilwaukeeToolApplication.getAppContext());
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        Point newSize = new Point();
        newSize.x = metrics.widthPixels;
        newSize.y = metrics.heightPixels;

        //LOGD(TAG, "Screen Size: " + newSize.x + ", " + newSize.y);

        return newSize;
    }

    public static Point fittedImageToDisplay(Point origImageSize, Context context) {

        Point displaySize = UIUtils.getScreenSize(context);

        Point newImageSize = new Point();
        newImageSize.x = origImageSize.x;
        newImageSize.y = origImageSize.y;

        double scaleX =  (displaySize.x * 1.0) / (origImageSize.x * 1.0);
        double scaleY =  (displaySize.y * 1.0) / (origImageSize.y * 1.0);

        if (scaleX < 1.0 && scaleY < 1.0) {

            // Scale by the larger of the two
            if (scaleX > scaleY) {
                newImageSize.x = displaySize.x;
                newImageSize.y = (int)((origImageSize.y * 1.0) * scaleX);
            } else if (scaleY > scaleX) {
                newImageSize.y = displaySize.y;
                newImageSize.x = (int)((origImageSize.x * 1.0) * scaleY);
            } else {
                newImageSize.x = displaySize.x;
                newImageSize.y = displaySize.y;
            }
        }
        return newImageSize;
    }

    public static boolean didTouchOccurInView(Context context, MotionEvent event, View view, Point maxSize) {

        if (maxSize == null) {
            maxSize = new Point(view.getWidth(), view.getHeight());
        }

        int touchX = (int)event.getRawX();
        int touchY = (int)event.getRawY();
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        Point start = new Point();
        Point end = new Point();

        start.x = location[0];
        start.y = location[1];

        end.x = start.x + UIUtils.getPixels(maxSize.x, context);
        end.y = start.y + UIUtils.getPixels(maxSize.y, context);

        if ((touchX >= start.x) && (touchX <= end.x) && (touchY >= start.y) && (touchY <= end.y)) {
            return true;
        }

        return false;
    }

    public static int getStatusBarHeight(Activity activity) {
        // Update the scale
        Rect rectangle = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarTop = rectangle.top;
        int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int statusbarHeight = Math.abs(statusBarTop - contentViewTop);
        return statusbarHeight;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = MilwaukeeToolApplication.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MilwaukeeToolApplication.getAppContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight(Context context) {

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize });
        int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarHeight;
    }


    public static boolean isMotionEventWithinViewBounds(View view, MotionEvent event) {
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();

        if (event.getRawX()< x || event.getRawX()> x + w || event.getRawY()< y || event.getRawY()> y + h) {
            return false;
        }
        return true;
    }

    public static void hideKeyboard(Activity activity) {

        // Check if no view has focus
        View view = activity.getCurrentFocus();
        if (view != null) {
            // Hide the keyboard, if shown
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboard(Activity activity, View view) {

        if (view != null) {
            // Hide the keyboard, if shown
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int determineNumberOfItemsToDisplay(Activity activity, int listItemHeight) {

        // Determine the height of the screen
        Point screenSize = UIUtils.getScreenSize(activity);

        int actionBarHeight = UIUtils.getActionBarHeight(activity);
        int statusBarHeight = UIUtils.getStatusBarHeight();
        int pagerTabStripHeight = UIUtils.getPixels(45);

        // Determine the top header space (actionbar +  pager tab strip + status bar)
        int remainingHeight = screenSize.y - (actionBarHeight + statusBarHeight + pagerTabStripHeight);

        int numberOfListItems = (remainingHeight / listItemHeight) + 1;

        LOGD(TAG, "Number of list items for search results: " + numberOfListItems);

        return numberOfListItems;
    }
}
