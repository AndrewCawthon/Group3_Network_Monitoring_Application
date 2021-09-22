/*-- 

 Copyright (C) 2001-2002 Anthony Eden.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The name "Java Network Monitor" must not be used to endorse or 
    promote products derived from this software without prior written 
	permission.  For written permission, please contact 
	me@anthonyeden.com.
 
 4. Products derived from this software may not be called 
    "Java Network Monitor", nor may "Java Network Monitor" appear in 
	their name, without prior written permission from Anthony Eden 
	(me@anthonyeden.com).
 
 In addition, I request (but do not require) that you include in the 
 end-user documentation provided with the redistribution and/or in the 
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by
      Anthony Eden (http://www.anthonyeden.com/)."

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT, 
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING 
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 POSSIBILITY OF SUCH DAMAGE.

 For more information on Java Network Monitor, please see 
 <http://jnm.sf.net/>.
 
 Some portions of the code below are:
 Copyright (C)1996,1998 by Jef Poskanzer <jef@acme.com>.  All rights reserved.
 
 */
 
package com.anthonyeden.jnm.util;

public class Utilities{
	
	/**	Encode the given String in Base 64 encoding.  This method was
		written and is copyrighted by Jef Poskanzer <jef@acme.com>
		
		@param srcString The source String
		@return The Base64 encoded String
	*/
	
	public static String base64Encode(String srcString){
		byte[] src = new byte[srcString.length()];
		srcString.getBytes(0, src.length, src, 0);
		return base64Encode(src);
	}
	
	/**	Encode the given byte array in Base 64 encoding.  This method was
		written and is copyrighted by Jef Poskanzer <jef@acme.com>
		
		@param src The source array
		@return The Base64 encoded String
	*/
	
	public static String base64Encode(byte[] src){
		StringBuffer encoded = new StringBuffer();
		int i, phase = 0;
		char c = 0;
	
		for(i = 0; i < src.length; ++i){
			switch(phase){
				case 0:
					c = b64EncodeTable[( src[i] >> 2 ) & 0x3f];
					encoded.append( c );
					c = b64EncodeTable[( src[i] & 0x3 ) << 4];
					encoded.append( c );
					++phase;
					break;
				case 1:
					c = b64EncodeTable[
						( b64DecodeTable[c] | ( src[i] >> 4 ) ) & 0x3f];
					encoded.setCharAt( encoded.length() - 1, c );
					c = b64EncodeTable[( src[i] & 0xf ) << 2];
					encoded.append( c );
					++phase;
					break;
				case 2:
					c = b64EncodeTable[
						( b64DecodeTable[c] | ( src[i] >> 6 ) ) & 0x3f];
					encoded.setCharAt( encoded.length() - 1, c );
					c = b64EncodeTable[src[i] & 0x3f];
					encoded.append( c );
					phase = 0;
					break;
			}
		}
		
		/* Pad with ='s. */
		while ( phase++ < 3 )
			encoded.append( '=' );
			
		return encoded.toString();
	}
	
	private static char b64EncodeTable[] = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',  // 00-07
		'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',  // 08-15
		'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',  // 16-23
		'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',  // 24-31
		'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',  // 32-39
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v',  // 40-47
		'w', 'x', 'y', 'z', '0', '1', '2', '3',  // 48-55
		'4', '5', '6', '7', '8', '9', '+', '/'   // 56-63
	};
	
	private static int b64DecodeTable[] = {
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 00-0F
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 10-1F
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,62,-1,-1,-1,63,  // 20-2F
		52,53,54,55,56,57,58,59,60,61,-1,-1,-1,-1,-1,-1,  // 30-3F
		-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,  // 40-4F
		15,16,17,18,19,20,21,22,23,24,25,-1,-1,-1,-1,-1,  // 50-5F
		-1,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,  // 60-6F
		41,42,43,44,45,46,47,48,49,50,51,-1,-1,-1,-1,-1,  // 70-7F
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 80-8F
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // 90-9F
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // A0-AF
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // B0-BF
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // C0-CF
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // D0-DF
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,  // E0-EF
		-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1   // F0-FF
	};
	
}
