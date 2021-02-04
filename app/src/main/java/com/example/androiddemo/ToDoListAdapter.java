package com.example.androiddemo;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.TodoListHolder>
{
    private Context mContext;
    private DatabaseHelper databaseHelper;
    private List<ToDo> todos;
    private OnItemClickListener mListener;

    //Constructor
    public ToDoListAdapter(Context context, List<ToDo> alltodo) {
        mContext = context;
        todos = alltodo;
        databaseHelper = new DatabaseHelper(mContext);
    }

    @NonNull
    @Override
    public ToDoListAdapter.TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.todolist_item, parent, false);
        return new TodoListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoListAdapter.TodoListHolder holder, int position) {
        ToDo currentTodo = todos.get(position);

        holder.textTitle.setText(currentTodo.getTodoTitle());
        holder.textID.setText("ID: " + currentTodo.getTodoID());
        holder.textDesc.setText("Description: " +currentTodo.getTodoDesc());
        holder.textDate.setText("Date: " +currentTodo.getTodoDate());
        holder.textStat.setText("Status: " +currentTodo.getTodoStatus());
        if(currentTodo.getTodoStatus().equals("Complete")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.checkBoxStat.setChecked(true);
                }
            }, 200);
        }

        holder.checkBoxStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isChecked = ((CheckBox)arg0).isChecked();
                if (isChecked){
                    currentTodo.setTodoStatus("Complete");
                }else{
                    currentTodo.setTodoStatus("Incomplete");
                }
                databaseHelper.updateToDo(currentTodo);
                holder.textStat.setText("Status: " +currentTodo.getTodoStatus());
            }
        });

    }


    @Override
    public int getItemCount() {
        return todos.size();
    }

    //layout template
    public class TodoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textTitle;
        public TextView textID;
        public TextView textDesc;
        public TextView textDate;
        public TextView textStat;
        public CheckBox checkBoxStat;

        //create an ImageView to insert memory photo and TextViews to insert memory info(people, date, place)
        public TodoListHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_itemtitle);
            textID = itemView.findViewById(R.id.text_itemid);
            textDesc = itemView.findViewById(R.id.text_itemdesc);
            textDate = itemView.findViewById(R.id.text_itemdate);
            textStat = itemView.findViewById(R.id.text_itemstat);
            checkBoxStat = itemView.findViewById(R.id.cb_status);
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
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
