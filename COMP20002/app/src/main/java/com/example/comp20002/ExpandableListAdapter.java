package com.example.comp20002;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;  // List of group headers
    private HashMap<String, List<String>> listDataChild;  // Child items for each header

    // Constructor
    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listDataChild) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

    // Returns the number of groups (headers)
    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    // Returns the number of child items in each group
    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).size();
    }

    // Returns the group at the specified position
    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    // Returns the child at the specified position
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    // Returns the ID of the group (used internally, can return groupPosition)
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Returns the ID of the child (used internally, can return childPosition)
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Returns whether the group ID is stable (should be true to avoid UI issues)
    @Override
    public boolean hasStableIds() {
        return true;
    }

    // Get the view for the group header
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_group, null);
        }

        TextView listHeader = convertView.findViewById(R.id.listHeader);
        listHeader.setText(headerTitle);

        return convertView;
    }

    // Get the view for each child item in the expandable list
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_item, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.listItem);
        txtListChild.setText(childText);

        return convertView;
    }

    // Returns whether the group is clickable (expandable)
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
