package com.telekurye.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.telekurye.kml.shapeInfo;
import com.telekurye.maks2.MissionControl;
import com.telekurye.mobileui.R;
import com.telekurye.tools.EnumBuildingTypes;
import com.telekurye.tools.Tools;

public class CustomListAdapter extends BaseAdapter implements OnClickListener {

	private Activity		act;
	private LayoutInflater	inflater;
	private List<shapeInfo>	missions;

	private int				counter				= 0;
	private int				totalMissioncount	= 0;

	public CustomListAdapter(Activity activity, List<shapeInfo> movieItems) {

		act = activity;
		missions = movieItems;
		totalMissioncount = missions.size();
	}

	public void clear() {
		missions.clear();
	}

	@Override
	public int getCount() {
		return missions.size();
	}

	@Override
	public Object getItem(int location) {
		return missions.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	protected static class RowViewHolder {

		public int				position;
		public MissionControl	feedback;
		public MissionControl	missionControl;

		// public Button btnCtrlSave;
		// public Button btnCtrlTypeId;
		// public Button btnCtrlFloorCount;
		// public Button btnCtrlIndependentCount;
		// public TextView tvCtrlTypeId;
		// public TextView tv_item_order;
		// public TextView tvCtrlFloorCount;
		// public TextView tvCtrlIndependentSectionCount;
		// public TextView tv_item_address;
		public TextView			tvCtrlBuildingNumber;

		public TextView			tvCtrlBuildingType;
		public TextView			tvCtrlIndependentSectionType;

		public RowViewHolder(View view) {

			tvCtrlBuildingType = (TextView) view.findViewById(R.id.tv_ctrlBuildingType);
			tvCtrlIndependentSectionType = (TextView) view.findViewById(R.id.tv_ctrl_indSecType);

			tvCtrlBuildingNumber = (TextView) view.findViewById(R.id.tvCtrlBuildingNumber);
			// tv_item_address = (TextView) view.findViewById(R.id.tv_item_address);
			// tvCtrlTypeId = (TextView) view.findViewById(R.id.tvCtrlBuildingType);
			// tvCtrlFloorCount = (TextView) view.findViewById(R.id.tvCtrlFloorCount);
			// tvCtrlIndependentSectionCount = (TextView) view.findViewById(R.id.tvCtrlIndependentSectionCount);
			// tv_item_order = (TextView) view.findViewById(R.id.tv_item_order);
			// btnCtrlSave = (Button) view.findViewById(R.id.btnCtrlSave);
			// btnCtrlTypeId = (Button) view.findViewById(R.id.btnCtrlBuildingType);
			// btnCtrlFloorCount = (Button) view.findViewById(R.id.btnCtrlFloorCount);
			// btnCtrlIndependentCount = (Button) view.findViewById(R.id.btnCtrlIndependentCount);
		}

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (view == null)
			view = inflater.inflate(R.layout.list_row, null);

		RowViewHolder viewHolder = new RowViewHolder(view);

		shapeInfo mcCurrent = missions.get(position);

		// viewHolder.btnCtrlTypeId.setOnClickListener(this);
		// viewHolder.btnCtrlFloorCount.setOnClickListener(this);
		// viewHolder.btnCtrlIndependentCount.setOnClickListener(this);
		// viewHolder.btnCtrlSave.setOnClickListener(this);

		if (mcCurrent == null) {
			return view;
		}

		// if (viewHolder.btnCtrlTypeId != null) {
		// viewHolder.btnCtrlTypeId.setText(mcCurrent.getTypeId() != 0 ? getNameById(mcCurrent.getTypeId()) : "-");
		// }
		//
		// if (viewHolder.btnCtrlFloorCount != null) {
		// viewHolder.btnCtrlFloorCount.setText(mcCurrent.getFloorCount() != 0 ? "" + mcCurrent.getFloorCount() : "-");
		// }
		//
		// if (viewHolder.btnCtrlIndependentCount != null) {
		// viewHolder.btnCtrlIndependentCount.setText(mcCurrent.getIndependentSectionCount() != 0 ? "" + mcCurrent.getIndependentSectionCount() : "-");
		// }

		// if (viewHolder.tvCtrlBuildingNumber != null) {
		// viewHolder.tvCtrlBuildingNumber.setText(mcCurrent.getBuildingNumber() != null ? mcCurrent.getBuildingNumber() : "25/A");
		// }

		// if (viewHolder.tvCtrlTypeId != null) {
		// viewHolder.tvCtrlTypeId.setText(mcCurrent.getTypeId() != 0 ? getNameById(mcCurrent.getTypeId()) : "-");
		// }
		//
		// if (viewHolder.tvCtrlFloorCount != null) {
		// viewHolder.tvCtrlFloorCount.setText(mcCurrent.getFloorCount() != 0 ? "" + mcCurrent.getFloorCount() : "-");
		// }
		//
		// if (viewHolder.tvCtrlIndependentSectionCount != null) {
		// viewHolder.tvCtrlIndependentSectionCount.setText(mcCurrent.getIndependentSectionCount() != 0 ? "" + mcCurrent.getIndependentSectionCount() : "-");
		// }

		// if (viewHolder.tv_item_address != null) {
		// viewHolder.tv_item_address.setText(Info.CityName + " / " + Info.CountyName + " / " + Info.DistrictName);
		// }

		// if (viewHolder.tv_item_order != null) {
		// viewHolder.tv_item_order.setText((position + 1) + "/" + totalMissioncount);
		// }

		if (viewHolder.tvCtrlBuildingType != null) {
			viewHolder.tvCtrlBuildingType.setText(mcCurrent.aciklama != null ? mcCurrent.aciklama : "Bilgi Yok");
		}

		if (viewHolder.tvCtrlIndependentSectionType != null) {
			viewHolder.tvCtrlIndependentSectionType.setText("" + (mcCurrent.count != 0 ? mcCurrent.count : 0));
		}

		viewHolder.position = position;
		// viewHolder.missionControl = mcCurrent;
		// viewHolder.feedback = mcCurrent;

		view.setTag(viewHolder);

		return view;
	}

	@Override
	public void onClick(View v) {

		View view = (View) v.getParent().getParent().getParent();

		RowViewHolder holder = (RowViewHolder) view.getTag();

		int id = v.getId();

		// if (id == R.id.btnCtrlBuildingType) {
		// showDialogButtonClick(holder);
		// } else if (id == R.id.btnCtrlFloorCount) {
		// showDialogButtonClick(holder, getList(50), 1);
		// } else if (id == R.id.btnCtrlIndependentCount) {
		// showDialogButtonClick(holder, getList(50), 2);
		// } else if (id == R.id.btnCtrlSave) {
		// clickSaveButton(holder);
		// }

	}

	private void showDialogButtonClick(final RowViewHolder holder) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		// builder.setTitle("Seçiniz...");

		EnumBuildingTypes[] values = EnumBuildingTypes.values();

		CharSequence[] choiceList = new CharSequence[values.length];

		int selected = holder.missionControl.getTypeId() - 1;

		for (int i = 0; i < values.length; i++) {
			choiceList[i] = values[i].getName();
		}

		builder.setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				ListView lw = ((AlertDialog) dialog).getListView();
				String checkedItem = (String) lw.getAdapter().getItem(lw.getCheckedItemPosition());

				holder.feedback.setTypeId(getIdByName(checkedItem));
				// holder.btnCtrlTypeId.setText(checkedItem);

				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showDialogButtonClick(final RowViewHolder holder, List<String> list, final int mission) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setTitle("Seçiniz...");

		CharSequence[] choiceList = list.toArray(new CharSequence[list.size()]);

		int selected = 0;

		if (mission == 1) {
			selected = holder.missionControl.getFloorCount() - 1;
		}
		else if (mission == 2) {
			selected = holder.missionControl.getIndependentSectionCount() - 1;
		}

		builder.setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				ListView lw = ((AlertDialog) dialog).getListView();
				Integer checkedItem = Integer.parseInt((String) lw.getAdapter().getItem(lw.getCheckedItemPosition()));

				if (mission == 1) {
					holder.feedback.setFloorCount(checkedItem);
					// holder.btnCtrlFloorCount.setText("" + checkedItem);
				}
				else if (mission == 2) {
					holder.feedback.setIndependentSectionCount(checkedItem);
					// holder.btnCtrlIndependentCount.setText("" + checkedItem);
				}

				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private void clickSaveButton(RowViewHolder holder) {

		if (Tools.isConnectingToInternet(act)) {
			holder.feedback.setIsGreen(true);
			holder.feedback.Update();

			// holder.tvCtrlTypeId.setText(getNameById(holder.feedback.getTypeId()));
			// holder.tvCtrlFloorCount.setText("" + holder.feedback.getFloorCount());
			// holder.tvCtrlIndependentSectionCount.setText("" + holder.feedback.getIndependentSectionCount());

			AsyncTaskSendData sendData = new AsyncTaskSendData(act);
			sendData.execute(holder.feedback);
		}
		else {
			Tools.showShortCustomToast(act, "Ýnternet Baðlantýsý Bulunamadý!");
		}

	}

	private List<String> getList(int maxValue) {

		List<String> list = new ArrayList<String>();

		for (int i = 1; i <= maxValue; i++) {
			list.add(Integer.toString(i));
		}

		return list;
	}

	private String getNameById(int id) {
		for (EnumBuildingTypes type : EnumBuildingTypes.values()) {
			if (type.getId() == id) {
				return type.getName();
			}
		}
		return "";
	}

	private int getIdByName(String name) {
		for (EnumBuildingTypes type : EnumBuildingTypes.values()) {
			if (type.getName().equals(name)) {
				return type.getId();
			}
		}
		return 0;
	}

}