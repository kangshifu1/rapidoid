package org.rapidoid.buffer;

/*
 * #%L
 * rapidoid-buffer
 * %%
 * Copyright (C) 2014 - 2016 Nikolche Mihajlovski and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.rapidoid.annotation.Authors;
import org.rapidoid.annotation.Since;
import org.rapidoid.bytes.Bytes;
import org.rapidoid.data.Range;
import org.rapidoid.data.Ranges;
import org.rapidoid.wrap.IntWrap;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

@Authors("Nikolche Mihajlovski")
@Since("4.1.0")
public class SynchronizedBuf implements Buf {

	private final Buf buf;

	public SynchronizedBuf(Buf buf) {
		this.buf = buf;
	}

	@Override
	public synchronized byte get(int position) {
		return buf.get(position);
	}

	@Override
	public synchronized void put(int position, byte value) {
		buf.put(position, value);
	}

	@Override
	public synchronized void append(byte value) {
		buf.append(value);
	}

	@Override
	public synchronized void put(int position, byte[] bytes, int offset, int length) {
		buf.put(position, bytes, offset, length);
	}

	@Override
	public synchronized int size() {
		return buf.size();
	}

	@Override
	public synchronized void append(ByteBuffer wrap) {
		buf.append(wrap);
	}

	@Override
	public synchronized int append(ReadableByteChannel channel) throws IOException {
		return buf.append(channel);
	}

	@Override
	public synchronized int append(String s) {
		return buf.append(s);
	}

	@Override
	public synchronized void append(byte[] bytes) {
		buf.append(bytes);
	}

	@Override
	public synchronized void append(byte[] bytes, int offset, int length) {
		buf.append(bytes, offset, length);
	}

	@Override
	public synchronized String data() {
		return buf.data();
	}

	@Override
	public synchronized int writeTo(WritableByteChannel channel) throws IOException {
		return buf.writeTo(channel);
	}

	@Override
	public synchronized int writeTo(ByteBuffer buffer) {
		return buf.writeTo(buffer);
	}

	@Override
	public synchronized void deleteBefore(int position) {
		buf.deleteBefore(position);
	}

	@Override
	public synchronized void deleteAfter(int position) {
		buf.deleteAfter(position);
	}

	@Override
	public synchronized void deleteLast(int count) {
		buf.deleteLast(count);
	}

	@Override
	public synchronized int unitCount() {
		return buf.unitCount();
	}

	@Override
	public synchronized int unitSize() {
		return buf.unitSize();
	}

	@Override
	public synchronized void clear() {
		buf.clear();
	}

	@Override
	public synchronized String get(Range range) {
		return buf.get(range);
	}

	@Override
	public synchronized long getN(Range range) {
		return buf.getN(range);
	}

	@Override
	public synchronized boolean isSingle() {
		return buf.isSingle();
	}

	@Override
	public synchronized ByteBuffer getSingle() {
		return buf.getSingle();
	}

	@Override
	public synchronized ByteBuffer first() {
		return buf.first();
	}

	@Override
	public synchronized int putNumAsText(int position, long num, boolean forward) {
		return buf.putNumAsText(position, num, forward);
	}

	@Override
	public synchronized void get(Range range, byte[] dest, int offset) {
		buf.get(range, dest, offset);
	}

	@Override
	public synchronized byte next() {
		return buf.next();
	}

	@Override
	public synchronized void back(int count) {
		buf.back(count);
	}

	@Override
	public synchronized byte peek() {
		return buf.peek();
	}

	@Override
	public synchronized boolean hasRemaining() {
		return buf.hasRemaining();
	}

	@Override
	public synchronized int remaining() {
		return buf.remaining();
	}

	@Override
	public synchronized int position() {
		return buf.position();
	}

	@Override
	public synchronized int limit() {
		return buf.limit();
	}

	@Override
	public synchronized void position(int position) {
		buf.position(position);
	}

	@Override
	public synchronized void limit(int limit) {
		buf.limit(limit);
	}

	@Override
	public synchronized void upto(byte value, Range range) {
		buf.upto(value, range);
	}

	@Override
	public synchronized ByteBuffer exposed() {
		return buf.exposed();
	}

	@Override
	public synchronized void scanUntil(byte value, Range range) {
		buf.scanUntil(value, range);
	}

	@Override
	public synchronized void scanWhile(byte value, Range range) {
		buf.scanWhile(value, range);
	}

	@Override
	public synchronized void skip(int count) {
		buf.skip(count);
	}

	@Override
	public synchronized ByteBuffer bufAt(int index) {
		return buf.bufAt(index);
	}

	@Override
	public synchronized int bufCount() {
		return buf.bufCount();
	}

	@Override
	public synchronized int bufferIndexOf(int position) {
		return buf.bufferIndexOf(position);
	}

	@Override
	public synchronized int bufferOffsetOf(int position) {
		return buf.bufferOffsetOf(position);
	}

	@Override
	public synchronized OutputStream asOutputStream() {
		return buf.asOutputStream();
	}

	@Override
	public synchronized String asText() {
		return buf.asText();
	}

	@Override
	public synchronized Bytes bytes() {
		return buf.bytes();
	}

	@Override
	public synchronized void scanLn(Range range) {
		buf.scanLn(range);
	}

	@Override
	public synchronized void scanLnLn(Ranges ranges) {
		buf.scanLnLn(ranges);
	}

	@Override
	public synchronized void scanN(int count, Range range) {
		buf.scanN(count, range);
	}

	@Override
	public synchronized String readLn() {
		return buf.readLn();
	}

	@Override
	public synchronized String readN(int count) {
		return buf.readN(count);
	}

	@Override
	public synchronized byte[] readNbytes(int count) {
		return buf.readNbytes(count);
	}

	@Override
	public synchronized void scanTo(byte sep, Range range, boolean failOnLimit) {
		buf.scanTo(sep, range, failOnLimit);
	}

	@Override
	public synchronized int scanTo(byte sep1, byte sep2, Range range, boolean failOnLimit) {
		return buf.scanTo(sep1, sep2, range, failOnLimit);
	}

	@Override
	public synchronized void scanLnLn(Ranges ranges, IntWrap result, byte end1, byte end2) {
		buf.scanLnLn(ranges, result, end1, end2);
	}

	@Override
	public synchronized void setReadOnly(boolean readOnly) {
		buf.setReadOnly(readOnly);
	}

	@Override
	public synchronized int checkpoint() {
		return buf.checkpoint();
	}

	@Override
	public synchronized void checkpoint(int checkpoint) {
		buf.checkpoint(checkpoint);
	}

}
