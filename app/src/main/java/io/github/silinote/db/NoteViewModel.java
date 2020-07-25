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

package io.github.silinote.db;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

	private LiveData<List<Note>> mAllNotes;
	private Application mApplication;

	public NoteViewModel(Application application) {
		super(application);
		mApplication = application;
		mAllNotes = Note.all(mApplication);
	}

	public LiveData<List<Note>> getAllNotes() {
		return mAllNotes;
	}

	public LiveData<List<Note>> search(String searchWord) {
		return Note.search(searchWord, mApplication);
	}
}
