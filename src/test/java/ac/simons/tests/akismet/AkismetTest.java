/**
 * Created by Michael Simons, michael-simons.eu
 * and released under The BSD License
 * http://www.opensource.org/licenses/bsd-license.php
 *
 * Copyright (c) 2011, Michael Simons
 * All rights reserved.
 *
 * Redistribution  and  use  in  source   and  binary  forms,  with  or   without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source   code must retain   the above copyright   notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary  form must reproduce  the above copyright  notice,
 *   this list of conditions  and the following  disclaimer in the  documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name  of  michael-simons.eu   nor the names  of its contributors
 *   may be used  to endorse   or promote  products derived  from  this  software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS  PROVIDED BY THE  COPYRIGHT HOLDERS AND  CONTRIBUTORS "AS IS"
 * AND ANY  EXPRESS OR  IMPLIED WARRANTIES,  INCLUDING, BUT  NOT LIMITED  TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL  THE COPYRIGHT HOLDER OR CONTRIBUTORS  BE LIABLE
 * FOR ANY  DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR  CONSEQUENTIAL
 * DAMAGES (INCLUDING,  BUT NOT  LIMITED TO,  PROCUREMENT OF  SUBSTITUTE GOODS OR
 * SERVICES; LOSS  OF USE,  DATA, OR  PROFITS; OR  BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT  LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE  USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package ac.simons.tests.akismet;

import junit.framework.Assert;

import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import ac.simons.akismet.Akismet;
import ac.simons.akismet.AkismetException;

/**
 * @author Michael J. Simons
 */

public class AkismetTest {
	private final static String validApiKey;
	private final static String validApiConsumer;
	
	static {
		validApiKey = System.getProperty("akismetApiKey");
		validApiConsumer = System.getProperty("akismetConsumer");
		
		if(validApiKey == null || validApiConsumer == null)
			throw new RuntimeException("Both api key and consumer must be specified!");
	}
	
	@Test
	public void verify() throws AkismetException {
		final Akismet akismet = new Akismet();
		akismet.setHttpClient(new DefaultHttpClient());
		
		akismet.setApikey(validApiKey);		
		akismet.setApiConsumer(validApiConsumer);		
		Assert.assertTrue(akismet.verifyKey());
		
		akismet.setApikey("123test");		
		akismet.setApiConsumer("http://test.com");
		Assert.assertFalse(akismet.verifyKey());
		
		akismet.setApiEndpoint("test.com");
		akismet.setApikey("123test");		
		akismet.setApiConsumer("http://test.com");
		Assert.assertFalse(akismet.verifyKey());
	}
}