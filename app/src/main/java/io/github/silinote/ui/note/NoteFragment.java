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
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.silinote.R;
import io.github.silinote.db.Note;
import io.github.silinote.db.NoteViewModel;

public class NoteFragment extends Fragment {

	private NoteListAdapter mAdapter;
	private EditText mSearch;
	private NoteViewModel mNoteViewModel;

	private void newNote() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		final EditText note = new EditText(getActivity());
		note.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
		builder.setView(note);
		builder.setTitle(R.string.New_note);
		builder.setPositiveButton(R.string.save, (dialog, which) -> {
			new Note(note.getText().toString().replace("\r", "").replace("\n", "")).insert(getContext());
		});
		builder.setNeutralButton(R.string.cancel, (dialog, which) -> {

		});
		builder.show();

	}

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_note, container, false);
		RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
		mAdapter = new NoteListAdapter(getActivity());
		recyclerView.setAdapter(mAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
		mNoteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> mAdapter.setNotes(notes));
		mSearch = root.findViewById(R.id.search);
		mSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				search();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		Button newNote = root.findViewById(R.id.newNote);
		newNote.setOnClickListener(v -> newNote());
		return root;
	}

	private void search() {
		if (mSearch.getText().length() < 1)
			mNoteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> mAdapter.setNotes(notes));
		else
			mNoteViewModel.search(mSearch.getText().toString()).observe(getViewLifecycleOwner(),
					notes -> mAdapter.setNotes(notes));
	}

}