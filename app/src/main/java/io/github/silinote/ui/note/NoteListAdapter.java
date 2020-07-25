//    The GNU General Public License does not permit incorporating this program
//    into proprietary programs.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.silinote.ui.note;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.List;

import io.github.silinote.R;
import io.github.silinote.db.Note;
import io.github.silinote.ui.Alert;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

	static class NoteViewHolder extends RecyclerView.ViewHolder {
		private final TextView mTextViewNote;
		private final TextView mTextViewDate;
		private final View mBackGround;
		Note mNote;

		private NoteViewHolder(View itemView) {
			super(itemView);
			mTextViewNote = itemView.findViewById(R.id.textViewNote);
			mTextViewDate = itemView.findViewById(R.id.textViewDate);
			mBackGround = itemView.findViewById(R.id.backGround);
		}

		void copyNote(Context context) {
			ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clipData = ClipData.newPlainText(mNote.note, mNote.note);
			clipboardManager.setPrimaryClip(clipData);

			new Alert(context).copied();
		}

		private void openNote(Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);

			final EditText note = new EditText(context);
			note.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
			note.setText(mNote.note);
			builder.setView(note);
			builder.setTitle(R.string.Edit_note);

			builder.setPositiveButton(R.string.save, (dialog, which) -> {
				mNote.note = note.getText().toString().replace("\r", "").replace("\n", "");
				mNote.update(context);
			});
			builder.setNeutralButton(R.string.cancel, (dialog, which) -> {

			});
			builder.setNegativeButton(R.string.delete, (dialog, which) -> mNote.delete(context));
			builder.show();
		}

		void updateContent() {
			mTextViewNote.setText(mNote.note);
			mTextViewDate.setText(new PrettyTime().format(new Date(mNote.createdAt)));

			mTextViewNote.setOnClickListener(v -> openNote(v.getContext()));
			mTextViewDate.setOnClickListener(v -> openNote(v.getContext()));
			mBackGround.setOnClickListener(v -> openNote(v.getContext()));

			mTextViewNote.setLongClickable(true);
			mTextViewDate.setLongClickable(true);
			mBackGround.setLongClickable(true);

			mTextViewNote.setOnLongClickListener(v -> {
				copyNote(v.getContext());
				return true;
			});
			mTextViewDate.setOnLongClickListener(v -> {
				copyNote(v.getContext());
				return true;
			});
			mBackGround.setOnLongClickListener(v -> {
				copyNote(v.getContext());
				return true;
			});
		}
	}

	private final LayoutInflater mInflater;

	private List<Note> mNotes;

	NoteListAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount() {
		if (mNotes != null)
			return mNotes.size();
		else
			return 0;
	}

	@Override
	public void onBindViewHolder(@NonNull NoteListAdapter.NoteViewHolder holder, int position) {
		if (mNotes != null) {
			holder.mNote = mNotes.get(position);
			holder.updateContent();
		}
	}

	@Override
	public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = mInflater.inflate(R.layout.recycler_note, parent, false);

		return new NoteViewHolder(itemView);
	}

	void setNotes(List<Note> notes) {
		mNotes = notes;
		notifyDataSetChanged();
	}
}
