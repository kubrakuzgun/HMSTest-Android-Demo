package com.example.androiddemo;

import android.content.Context;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MeetingListHolder>
{
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private List<Meeting> meetings;
    private MeetingListAdapter.OnItemClickListener mListener;

    //Constructor
    public MeetingListAdapter(Context context, List<Meeting> allmeeting) {
        mContext = context;
        meetings = allmeeting;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public MeetingListAdapter.MeetingListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.todolist_item, parent, false);
        return new MeetingListHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull MeetingListAdapter.MeetingListHolder holder, int position) {
        Meeting currentMeeting = meetings.get(position);

        holder.textTitle.setText(currentMeeting.getMeetingTitle());
        holder.textID.setText("ID: " + currentMeeting.getMeetingID());
        holder.textStart.setText("Start: " +currentMeeting.getMeetingStart());
        holder.textDate.setText("Date: " +currentMeeting.getMeetingDate());
        holder.textEnd.setText("End: " +currentMeeting.getMeetingEnd());

    }


    @Override
    public int getItemCount() {
        return meetings.size();
    }

    //layout template
    public class MeetingListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textTitle;
        public TextView textID;
        public TextView textStart;
        public TextView textDate;
        public TextView textEnd;
        //create an ImageView to insert memory photo and TextViews to insert memory info(people, date, place)
        public MeetingListHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_meetingtitle);
            textID = itemView.findViewById(R.id.text_meetingid);
            textStart = itemView.findViewById(R.id.text_meetingstart);
            textDate = itemView.findViewById(R.id.text_meetingdate);
            textEnd = itemView.findViewById(R.id.text_meetingend);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }


        //when clicked to a memory view, get position
        @Override
        public void onClick(View v) {
            //if view has a click listener
            if (mListener != null) {
                //get position of clicked item
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    //call item click function
                    mListener.onItemClick(position);
                }
            }
        }

        //create menu
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Options");
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            //set click listener to edit option
            edit.setOnMenuItemClickListener(this);

            //set click listener to delete option
            delete.setOnMenuItemClickListener(this);
        }

        //when an edit menu item is clicked
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            //if somewhere on the menu is clicked
            if (mListener != null) {
                //get click position
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    //get clicked item's id
                    switch (item.getItemId()) {
                        //id is 1 - edit memory
                        case 1:
                            mListener.onEditClick(position);
                            return true;
                        //id is 2 - delete memory
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    //interface for item click, edit and delete operations
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    //set item click listener
    public void setOnItemClickListener(ToDoListAdapter.OnItemClickListener listener) {
        mListener = (OnItemClickListener) listener;
    }

}
