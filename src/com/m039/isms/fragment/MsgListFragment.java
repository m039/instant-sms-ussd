/** MsgListFragment.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.m039.isms.adapter.MsgCursorAdapter;
import com.m039.isms.db.DB;
import com.m039.isms.items.Msg;
import com.m039.mqst.R;

/**
 *
 *
 * Created: 12/03/13
 *
 * @author Mozgin Dmitry
 * @version
 * @since
 */
public class MsgListFragment extends ListFragment {

    public static final String TAG = "m039-MsgListFragment";

    public static MsgListFragment newInstance() {
        return new MsgListFragment();
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        Resources res = getResources();
        
        ListView list = getListView();
        list.setDivider(res.getDrawable(R.drawable.f_list__list__divider));
        list.setDividerHeight(res.getDimensionPixelSize(R.dimen.f_list__list__divider_height));
    }
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new MsgCursorAdapter(getActivity(), null));
        setListShown(false);
        getLoaderManager().initLoader(0, null, mLoaderCallabacks);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(), "" + id, Toast.LENGTH_SHORT).show();
    }

    private static class MsgResult {
        Cursor cursor = null;
        boolean isReadyToDeliver = false;
    }

    private static class MsgLoader extends AsyncTaskLoader<MsgResult> {
        MsgResult mResult = new MsgResult();

        public MsgLoader(Context ctx) {
            super (ctx);
        }

        @Override
        protected void onStartLoading() {
            Log.d(TAG, "onStartLoading");

            if(mResult.isReadyToDeliver) {
                deliverResult(mResult);
            } else {
                forceLoad();
            }
        }

        @Override
        public MsgResult loadInBackground () {
            Log.d(TAG, "loadInBackground");

            mResult.cursor = DB.getInstance(getContext())
                .getDBHelper()
                .getReadableDatabase()
                .query(Msg.SQL.TABLE,
                       null,
                       null,
                       null,
                       null,
                       null,
                       null);

            mResult.isReadyToDeliver = true;

            return mResult;
        }
    }

    private LoaderManager.LoaderCallbacks<MsgResult> mLoaderCallabacks =
        new LoaderManager.LoaderCallbacks<MsgResult>() {

        @Override
        public Loader<MsgResult>  onCreateLoader(int id, Bundle args) {
            return new MsgLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<MsgResult> loader, MsgResult data) {
            ListAdapter la = getListAdapter();
            if (la instanceof CursorAdapter) {
                CursorAdapter ca = (CursorAdapter) la;
                ca.swapCursor(data.cursor);
            }

            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<MsgResult> loader) {
        }
    };

} // MsgListFragment
