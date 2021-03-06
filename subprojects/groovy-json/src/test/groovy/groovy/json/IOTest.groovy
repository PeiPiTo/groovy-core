package groovy.json

import groovy.json.internal.CharBuf
import groovy.json.internal.IO

/**
 * Test the internal IO class
 *
 * @author Martin Stockhammer
 */
class IOTest extends GroovyTestCase {

	public static final String TEST_STRING = '{"results":[{"columns":["n"],"data":[{"row":[{"name":"Alin Coen Band","type":"group"}]}]}],"errors":[]}'
	
	int bufSize = 256
	
	void testReadProper() {
		IO io = new IO()
		ProperReader reader = new ProperReader();
		CharBuf buffer = io.read(reader, null, bufSize)
		int len = buffer.len()
		char[] rbuf = buffer.readForRecycle()
		String result = new String(rbuf, 0, len)
		assertEquals(TEST_STRING,result) 
	}

	/**
	 * See https://jira.codehaus.org/browse/GROOVY-7132
	 */
	void testReadBumpy() {
		IO io = new IO()
		BumpyReader reader = new BumpyReader();
		CharBuf buffer = io.read(reader, null, bufSize)
		int len = buffer.len()
		char[] rbuf = buffer.readForRecycle()
		String result = new String(rbuf,0,len)
		assertEquals(TEST_STRING,result) 
	}

}

/**
 * This reader fills the char array at certain points only partially
 * and returns a value < char_array.length
 */
class BumpyReader extends Reader {

	int index = 0;
	def stopIndex = [69, 84, 500]
	int nextStop=0;

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (index>=IOTest.TEST_STRING.size()) {
			return -1
		}
		int num=0
		while(num<len && index < stopIndex[nextStop] && index<IOTest.TEST_STRING.size()) {
			cbuf[off+num]=IOTest.TEST_STRING[index]
			num++
			index++
		}
		if(index==stopIndex[nextStop]) {
			nextStop++
		}
		return num;
	}

	@Override
	public void close() throws IOException {
	}
}

/**
 * This reader fills always the char array completely until reaching the end
 * of the string.
 */
class ProperReader extends Reader {

	int index = 0;

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		if (index>=IOTest.TEST_STRING.size()) {
			return -1
		}
		int num=0
		while(num<len && index<IOTest.TEST_STRING.size()) {
			cbuf[off+num]=IOTest.TEST_STRING[index]
			num++
			index++
		}
		return num;
	}

	@Override
	public void close() throws IOException {
	}
}
