package com.todo.ui.feature.tasks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.todo.R;
import com.todo.data.model.TaskModel;
import com.todo.di.activity.ActivityComponent;
import com.todo.ui.base.BaseActivity;
import com.todo.ui.feature.addedittask.AddEditTaskActivity;
import com.todo.ui.feature.login.LoginActivity;
import com.todo.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public final class TasksActivity extends BaseActivity implements TasksContract.View, TaskItemTouchHelper.TaskItemTouchHelperCallback {

    /********* Dagger Injected Fields  ********/

    @Inject
    TasksContract.Presenter presenter;

    @Inject
    DateUtils dateUtils;

    /********* Butterknife View Binding Fields  ********/

    @BindView(R.id.tasks_recyclerview_tasks)
    RecyclerView recyclerViewTasks;

    @BindView(R.id.tasks_linear_layout_empty)
    LinearLayout linearLayoutEmpty;

    private TasksAdapter tasksAdapter;


    /********* Static Methods ********/


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TasksActivity.class);
        context.startActivity(intent);
    }

    public static Intent getIntentForNotification(Context context) {
        Intent intent = new Intent(context, TasksActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /********* Lifecycle Methods Implementation ********/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_activity);
        setUnbinder(ButterKnife.bind(this));
        initializeToolbar();
        initializeRecyclerView();
        presenter.attachView(this);
        presenter.setTasksSortType(TasksSortType.BY_DATE);
        presenter.getTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tasks_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.tasks_menu_item_by_date:
                presenter.setTasksSortType(TasksSortType.BY_DATE);
                presenter.getTasks();
                return true;
            case R.id.tasks_menu_item_by_priority:
                presenter.setTasksSortType(TasksSortType.BY_PRIORITY);
                presenter.getTasks();
                return true;
            case R.id.tasks_menu_item_by_name:
                presenter.setTasksSortType(TasksSortType.BY_TITLE);
                presenter.getTasks();
                return true;
            case R.id.tasks_menu_item_logout:
                presenter.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /********* DaggerActivity Inherited Methods ********/
    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /********* TasksContract.View Inherited Methods ********/

    @Override
    public void showAddEditTaskActivity() {
        AddEditTaskActivity.startActivity(this);
    }

    @Override
    public void showLoginUi() {
        LoginActivity.startActivity(this);
    }

    @Override
    public void showTasks(List<TaskModel> taskModels) {
        tasksAdapter.clear();
        tasksAdapter.addAll(taskModels);
    }

    @Override
    public void showTasksEmptyView() {
        recyclerViewTasks.setVisibility(View.GONE);
        linearLayoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTasksEmptyView() {

        linearLayoutEmpty.setVisibility(View.GONE);
        recyclerViewTasks.setVisibility(View.VISIBLE);
    }


    /********* TaskItemTouchHelper.TaskItemTouchHelperCallback Implemented Methods ********/

    @Override
    public void onTaskDeleted(final int position) {
        final TaskModel removedTaskModel = tasksAdapter.remove(position);
        addDisposable(showSnackBar(R.string.tasks_message_deleted, R.string.tasks_action_undo)
                .subscribe(undo -> {
                    if (undo) {
                        tasksAdapter.add(position, removedTaskModel);
                    } else {
                        // remove from backend
                        presenter.deleteTask(removedTaskModel);
                    }
                }));
    }

    @Override
    public void onTaskCompleted(int position) {
        presenter.updateTask(tasksAdapter.get(position));
        final TaskModel completedTaskModel = tasksAdapter.remove(position);
        addDisposable(showSnackBar(R.string.tasks_message_completed, R.string.tasks_action_undo)
                .subscribe(undo -> {
                    if (undo) {
                        tasksAdapter.add(position, completedTaskModel);
                    } else {
                        completedTaskModel.setCompleted(true);
                        presenter.updateTask(completedTaskModel);
                    }
                }));
    }

    /********* Butterknife Binded Methods  ********/

    @OnClick(R.id.tasks_button_add_task)
    public void buttonAddTaskClicked() {
        presenter.addTask();
    }

    /********* Member Methods  ********/

    private void initializeToolbar() {
        Toolbar myToolbar = findViewById(R.id.tasks_toolbar);
        setSupportActionBar(myToolbar);
    }

    private void initializeRecyclerView() {

        tasksAdapter = new TasksAdapter(dateUtils, new ArrayList<>());
        recyclerViewTasks.setAdapter(tasksAdapter);
        addDisposable(tasksAdapter.onItemClick().subscribe(this::onTaskSelected));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewTasks.setLayoutManager(linearLayoutManager);
        recyclerViewTasks.setItemAnimator(new DefaultItemAnimator());
        recyclerViewTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new TaskItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerViewTasks);

    }

    private void onTaskSelected(final TaskModel taskModel) {
        AddEditTaskActivity.startActivity(this, taskModel);
    }


}
