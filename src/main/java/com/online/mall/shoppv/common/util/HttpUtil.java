package com.online.mall.shoppv.common.util;


import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


public class HttpUtil {

	
    private static CloseableHttpClient httpClient;
    private static SSLContext       ctx             = null;
    private static SSLConnectionSocketFactory sslFactory   = null;
    
//  连接超时时间，默认5秒
    private static int connectTimeout = 2000;

    //传输超时时间，默认15秒
    private static int socketTimeout = 30000;
    
    private static int rquestConnctTimeout = 2000;
	
    private static RequestConfig proxyConfig;
    private static RequestConfig requestConfig;
    private static class DefaultTrustManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }
    
    static{
    	try {
    		ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() },
                new SecureRandom());
            sslFactory = new SSLConnectionSocketFactory(
            		ctx,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			init();
		} catch (Exception e) {
//			log.error(e.getMessage(), e);
		}
    }
    
    
	private static void init() throws IOException, KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException {
    	Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslFactory).build();
    	PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connManager.setDefaultSocketConfig(socketConfig);
		
		MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(100).setMaxLineLength(2000).build();
		ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).setMessageConstraints(messageConstraints).build();
		connManager.setDefaultConnectionConfig(connectionConfig);
		
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(100);
//		HttpHost proxy = new HttpHost(ConfigUtils.getProperty("proxy.host"),Integer.parseInt(ConfigUtils.getProperty("proxy.port")), "http");    
        //根据默认超时限制初始化requestConfig
        requestConfig = RequestConfig.custom()
        .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(rquestConnctTimeout).build();
        httpClient = HttpClients.custom().setConnectionManager(connManager)
                .build();
    }
	
	
	public static HttpResponse proxyPost(String url, String content)throws Exception
	{
		 String result = "";
		 HttpPost httpPost = new HttpPost(url);
		 httpPost.setEntity(new StringEntity(content,"utf-8"));
		 HttpEntity entity = null;
		 HttpResponse response = null;
		 try {
			 response = httpClient.execute(httpPost);
		 } catch (Exception e) {
			 e.printStackTrace();
		 } finally {
			 httpPost.abort();
			 if(entity!=null)
			 {
				 EntityUtils.consume(entity);
			 }
		 }
		 return response;
	}
	
	
	 public static String post(String url, String content) throws IOException
	 {
		 String result = "";
		 HttpPost httpPost = new HttpPost(url);
		 httpPost.addHeader("Accept", "application/json");
		 httpPost.addHeader("User-Agent", "pccc-pcs");
		 httpPost.addHeader("Content-Type", "application/json");
		 httpPost.setEntity(new StringEntity(content,"utf-8"));
		 httpPost.setConfig(proxyConfig);
		 HttpEntity entity = null;
		 try {
			 HttpResponse response = httpClient.execute(httpPost);
			 entity = response.getEntity();
			 if(200!=response.getStatusLine().getStatusCode())
			 {
//				 log.error("response status error|"+response.getStatusLine().getStatusCode());
				 return result;
			 }
			 result = EntityUtils.toString(entity, "UTF-8");
		 } catch (Exception e) {
			 e.printStackTrace();
		 } finally {
			 httpPost.abort();
			 if(entity!=null)
			 {
				 EntityUtils.consume(entity);
			 }
		 }
		 return result;
	 }
	 
	 
	 public static void main(String[] args) {
		try {
			//\"S_contactName\":\"jugg\",\"S_contactInfoType\":\"OTHER\",
			
			final SimpleDateFormat spd = new SimpleDateFormat("yyyyMMddHHmmss");
			int i = 0;
			List<Thread> list = new ArrayList<Thread>();
			while(i<1)
			{
				i++;
				list.add(new Thread(new Runnable() {
					
					public void run() {
						try {
//							System.out.println(post("http://182.180.117.147:9527/api/J_PCS_022_0404", "{\"Head\": {},\"Body\":{\"MrchntTraceNo\":\"20180329362923578\",\"CstmrDiscountAmt\":\"200\",\"TrxAmt\":\"4800\",\"TrxProcCode\":\"130112\",\"FrontUrl\":\"\",\"AcctType\":\"0111\",\"TrxPoints\":\"0\",\"PayProdSubTp\":\"1306\",\"Type\":\"0\",\"BackUrl\":\"\",\"AcctPin\":\"\",\"MrchntNo\":\"301001900001323\",\"MrchntTrxDate\":\"20180329\",\"AcctId\":\"4349100117157644\",\"CstmrDsctVoucher\":\"0550600200\",\"PayDsctName\":\"\",\"AcctName\":\"全渠道\",\"IdNo\":\"341126197709218366\",\"MrchntOrdrId\":\"20180329162910089\",\"PointOperateType\":\"\",\"MrchntOrdrTotalAmt\":\"5000\",\"TerminalInfo\":\"12345678\",\"ActivityDiscountAmt\":\"0\",\"TrxCcyCd\":\"156\",\"PhoneNo\":\"13552535506\",\"InstlmtNo\":\"0\",\"CstmrId\":\"25080074\",\"SecMrchntNo\":\"\",\"OrderType\":\"0\",\"SecMrchntName\":\"\",\"MrchntOrdrAmt\":\"5000\",\"TestFlag\":\"0\",\"MrchntTrxTime\":\"153136\",\"Location\":\"\",\"IdTp\":\"01\",\"PayDsctVoucher\":\"\",\"PointsDeductionAmt\":\"0\",\"VerifiedFlag\":\"3\",\"MrchntDiscountAmt\":\"0\",\"PayPin\":\"d85r+eUV5rwEImw3Tio9h66aKl5BHt4ZbldrqY/wGJc1ko+5UlcnMLkSNzvuGOaXnGVbJkjc5pCIutN2y8Y5K10aLlaljukNqI+5uOq5HvTu/C/AZOOLiocKP0zgBMTfnOmRCosM2uAjYkLlQBwf/IB+56SV1HUjtRgiLf/CP8s=\",\"PayProdTp\":\"13\",\"ChannelType\":\"02\",\"ReqIp\":\"\",\"SmsCode\":\"208455\",\"FrontFailUrl\":\"\"}}"));
//							piap_iep_trade_touda.YDHL-DEV1.apps.paasdev.pccc.com
							System.out.println(post("http://piap_iep_trade_touda.YDHL-SIT.apps.paastest.pccc.com/piap_iep_trade_touda/J_IEP_002_2008", "{\"Head\":{},\"Body\":{\"s_version\":\"1.0\",\"channelId\":\"iep\",\"iepTrxTime\":\""+spd.format(new Date())+"\",\"customerNo\":\"25080074\",\"sceneId\":\"W0\", \"goodsId\":\"1\",\"shopList\":[{\"mrchntId\":\"301001900001981\"}]}}"));
//							System.out.println(post("http://piap_iep_trade_touda.YDHL-SIT.apps.paastest.pccc.com/piap_iep_trade_touda/J_IEP_002_2003", "{\"Head\":{},\"Body\":{\"s_version\":\"1.0\",\"channelId\":\"iep\",\"iepTrxTime\":\""+spd.format(new Date())+"\",\"customerNo\":\"25080074\",\"sceneId\":\"W0\", \"dsplySerialCode\":\"0097020003\",\"mrchntId\":\"301001900001981\",\"orderNo\":\"P20180425980100000236\",\"sessionId\":\"112321343\"}}"));
//							System.out.println(post("http://piap_iep_trade_touda.YDHL-SIT.apps.paastest.pccc.com/piap_iep_trade_touda/J_IEP_002_2053", "{\"Head\":{},\"Body\":{\"s_version\":\"1.0\",\"channelId\":\"iep\",\"iepTrxTime\":\""+spd.format(new Date())+"\",\"customerNo\":\"25080074\",\"sceneId\":\"W0\", \"dsplySerialCode\":\"0097020003\",\"mrchntId\":\"301001900001981\",\"orderNo\":\"P20180425980100000236\"}}"));
//							System.out.println(post("http://localhost:8080/api-manager/test/api/createOrder","{\"mrchntNo\":\"3010848998\",\"mrchntTraceNo\":\"2019348745347567\",\"mrchntOrdrTotalAmt\":\"10\",\"mrchntNoDsctAmt\":\"10\"}"));
//							System.out.println(post("http://182.180.117.170:9087/pgs/J_PGS_022_0120", "{\"Body\":{\"S_externalId\":\"fc99c144063d4dd9ac0312fcdd947dd8\",\"S_mercName\":\"自动化商普1073030连锁\",\"S_source\":\"2088911212416201\",\"S_mcShortName\":\"自动化商普1073030\",\"ChannelType\":\"07\",\"S_business\":\"2015050700000000\",\"S_servicePhone\":\"20003200\",\"S_target\":\"true\",\"S_tag\":\"02\"},\"Head\":{}}"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}));
			}
			for(int j=0;j<list.size();j++)
			{
				list.get(j).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
