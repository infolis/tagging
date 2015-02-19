// *****************************************************************************
//
// Copyright (c) 2013 Christian Schreckenberger / Christian Meilicke / Kai Eckert (University of Mannheim)
//
// Permission is hereby granted, free of charge, to any person
// obtaining a copy of this software and associated documentation
// files (the "Software"), to deal in the Software without restriction,
// including without limitation the rights to use, copy, modify, merge,
// publish, distribute, sublicense, and/or sell copies of the Software,
// and to permit persons to whom the Software is furnished to do so,
// subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
// WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
// IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// *********************************************************************************

package de.unima.ki.infolis.lohai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class IflRecordSet implements Iterable<IflRecord> {
	
	private List<IflRecord> records;
	
	public IflRecordSet() {
		this.records = new ArrayList<IflRecord>();
	}
	
	public void addRecord(IflRecord record) {
		this.records.add(record);
	}

	
	

	public List<IflRecord> getRecords() {
		return this.records;
	}

	public int getSize() {
		return this.records.size();
	}

	@Override
	public Iterator<IflRecord> iterator() {
		// TODO Auto-generated method stub
		return this.records.iterator();
	}

}
