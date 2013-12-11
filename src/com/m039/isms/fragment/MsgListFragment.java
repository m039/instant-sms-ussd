/** MsgListFragment.java ---
 *
 * Copyright (C) 2013 Mozgin Dmitry
 *
 * Author: Mozgin Dmitry <flam44@gmail.com>
 *
 *
 */

package com.m039.isms.fragment;

import android.app.Activity;
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
import android.widget.AdapterView;
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

    public final int LOADER_ID_QUERY = 1;
    public final int LOADER_ID_INSERT = 2;

    public static final String EXTRA_MSG = "com.m039.isms.fragment.extra.msg";

    public static MsgListFragment newInstance() {
        return new MsgListFragment();
    }

    public interface OnMsgLongClickListener {
        public boolean onMsgLongClick (AdapterView<?> parent, View view, int position, long id);
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);

        Resources res = getResources();

        ListView list = getListView();
        list.setDivider(res.getDrawable(R.drawable.f_list__list__divider));
        list.setDividerHeight(res.getDimensionPixelSize(R.dimen.f_list__list__divider_height));

        list.setOnItemLongClickListener(mOnItemLongClickListener);

        setEmptyText(getString(R.string.f_msg_list__empty));
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new MsgCursorAdapter(getActivity(), null));
        setListShown(false);
        getLoaderManager().initLoader(LOADER_ID_QUERY, null, mLoaderCallabacks);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(getActivity(), "" + id, Toast.LENGTH_SHORT).show();
    }

    AdapterView.OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
                Activity a = getActivity();
                if (a instanceof OnMsgLongClickListener) {
                    return ((OnMsgLongClickListener) a).onMsgLongClick(parent, view, position, id);
                }

                return false;
            }
        };


    public void addMsg(Msg userCreatedMsg) {
        Bundle args = new Bundle();

        args.putParcelable(EXTRA_MSG, userCreatedMsg);

        getLoaderManager().restartLoader(LOADER_ID_INSERT, args, mLoaderCallabacks);
    }

    private static class MsgResult {
        Cursor cursor = null;
        boolean isReadyToDeliver = false;
        String toast = null;
    }

    private static class MsgQueryLoader extends AsyncTaskLoader<MsgResult> {
        MsgResult mQueryResult = new MsgResult();

        public MsgQueryLoader(Context ctx) {
            super (ctx);
        }

        @Override
        protected void onStartLoading() {
            Log.d(TAG, "onStartLoading");

            if(mQueryResult.isReadyToDeliver) {
                deliverResult(mQueryResult);
            } else {
                forceLoad();
            }
        }

        @Override
        public MsgResult loadInBackground () {
            Log.d(TAG, "loadInBackground");

            mQueryResult.cursor = DB.getInstance(getContext())
                .getDBHelper()
                .getReadableDatabase()
                .query(Msg.SQL.TABLE,
                       null,
                       null,
                       null,
                       null,
                       null,
                       null);

            mQueryResult.isReadyToDeliver = true;

            return mQueryResult;
        }
    }

    private static class MsgInsertLoader extends MsgQueryLoader  {
        Msg mMsg;

        public MsgInsertLoader(Context ctx, Msg msg) {
            super (ctx);

            mMsg = msg;
        }

        @Override
        public MsgResult loadInBackground () {
            Log.d(TAG, "loadInBackground");

            Context ctx = getContext();

            String fmt;

            if (DB.getInstance(ctx)
                .getDBHelper()
                .getWritableDatabase()
                .insert(Msg.SQL.TABLE, null, DB.values(mMsg)) != -1) {

                fmt = ctx.getString(R.string.f_msg_list__msg_added);

            } else {

                fmt = ctx.getString(R.string.f_msg_list__error__msg_added);

            }

            MsgResult result = super.loadInBackground();

            result.toast = String.format(fmt, mMsg.getDescription());

            return result;
        }
    }

    private LoaderManager.LoaderCallbacks<MsgResult> mLoaderCallabacks =
        new LoaderManager.LoaderCallbacks<MsgResult>() {

        @Override
        public Loader<MsgResult>  onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ID_QUERY) {
                return new MsgQueryLoader(getActivity());
            } else if(id == LOADER_ID_INSERT) {
                return new MsgInsertLoader(getActivity(), (Msg) args.getParcelable(EXTRA_MSG));
            }

            return null;
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

            if (data.toast != null) {
                Toast.makeText(getActivity(), data.toast, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<MsgResult> loader) {
        }
    };

} // MsgListFragment
