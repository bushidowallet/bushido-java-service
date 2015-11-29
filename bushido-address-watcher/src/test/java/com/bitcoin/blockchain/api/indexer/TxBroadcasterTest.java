package com.bitcoin.blockchain.api.indexer;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Jesion on 2015-03-19.
 */
public class TxBroadcasterTest {

    @Test
    @Ignore
    public void testBroadcastTx() throws Exception{
        TxBroadcaster broadcaster = new TxBroadcaster();
        broadcaster.init();
        Thread.sleep(5000);
        broadcaster.broadcastTx("0100000002e8e0297338c3198d5014a56f1ad7453620444f44940d0ce421fa5da1a9a90cf0000000006b48304502207fb4e9c4829c757335db69c729875143c6f498b18de7eb222e7bc6b8793cb774022100dde6460348e6828ce7e0699ba6031b5cd2bd3c63b1b71af6c80610577e2cdce501210290fd099b044b4fb90c5c583582335e66d5390c3e30adcd553edb9b95fa493a68fffffffff087371c127e81c4787ca31ece5218408cf491a4608f8d3559e6d0ac43e83872000000006a473044022002703a5db313270f84d99e3805640f4f3d2f93cf38c2298b0a4d7ba0edab350a022036495caf8860af91b60d31ab578ce8159078ad0a77acaf25dd02f29a5fbc898c01210231a7b9edb4e893117563d9ba74a24a7ba366235b3b71b3620bdae2d2045f5625ffffffff02d8d60000000000001976a914cfedbbc5fc5fd9665b548a66bdade69ca5d9ce2188ac983a0000000000001976a914d1056cf8b2328a939961fb3367c42e6ca21f913a88ac00000000");
        Thread.sleep(20000);
    }
}
