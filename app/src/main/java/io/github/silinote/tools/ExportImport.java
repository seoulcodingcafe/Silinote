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

package io.github.silinote.tools;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import io.github.silinote.db.Note;

public class ExportImport {

	private Context mContext;

	public ExportImport(Context context) {
		mContext = context;
	}

	public void export(Uri uri) throws IOException, NullPointerException {
		OutputStream os = mContext.getContentResolver().openOutputStream(uri);
		PrintStream ps = new PrintStream(os);
		List<Note> notes = Note.export(mContext);
		for (Note note : notes) {
			ps.println(note.note);
		}
		ps.flush();
		ps.close();
	}

	public void import_(Uri uri) throws IOException, NullPointerException {
		InputStream is = mContext.getContentResolver().openInputStream(uri);
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		while (br.ready() && (line = br.readLine()) != null) {
			line = line.replace("\r", "").replace("\n", "");
			if (line.length() > 0)
				new Note(line).insert(mContext);
		}
	}
}
