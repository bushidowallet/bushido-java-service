/**
 * Copyright 2011 bccapi.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bccapi.bitlib.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Utility for turning an amount of satoshis into a user friendly string. 1
 * satoshi == 0.000000001 BTC
 */
public class CoinUtil {

   private static final BigDecimal BTC_IN_SATOSHIS = new BigDecimal(100000000);
   private static final BigDecimal mBTC_IN_SATOSHIS = new BigDecimal(100000);
   private static final BigDecimal uBTC_IN_SATOSHIS = new BigDecimal(100);

   public enum Denomination {
      BTC(8, "BTC", "BTC", BTC_IN_SATOSHIS), mBTC(5, "mBTC", "mBTC", mBTC_IN_SATOSHIS), uBTC(2, "uBTC",
            "\u00B5BTC", uBTC_IN_SATOSHIS);

      private final int _decimalPlaces;
      private final String _asciiString;
      private final String _unicodeString;
      private final BigDecimal _oneUnitInSatoshis;

      private Denomination(int decimalPlaces, String asciiString, String unicodeString, BigDecimal oneUnitInSatoshis) {
         _decimalPlaces = decimalPlaces;
         _asciiString = asciiString;
         _unicodeString = unicodeString;
         _oneUnitInSatoshis = oneUnitInSatoshis;
      }

      public int getDecimalPlaces() {
         return _decimalPlaces;
      }

      public String getAsciiName() {
         return _asciiString;
      }

      public String getUnicodeName() {
         return _unicodeString;
      }

      public BigDecimal getOneUnitInSatoshis() {
         return _oneUnitInSatoshis;
      }

      @Override
      public String toString() {
         return _asciiString;
      }

      public static Denomination fromString(String string) {
         if (string.equals("BTC")) {
            return BTC;
         } else if (string.equals("mBTC")) {
            return mBTC;
         } else if (string.equals("uBTC")) {
            return uBTC;
         } else {
            return BTC;
         }
      }

   };

   /**
    * Number of satoshis in a Bitcoin.
    */
   public static final BigInteger BTC = new BigInteger("100000000", 10);

   /**
    * Get the given value in satoshis as a string on the form "10.12345"
    * denominated in BTC.
    * <p>
    * This method only returns necessary decimal points to tell the exact value.
    * If you wish to display all digits use
    * {@link CoinUtil#fullValueString(long)}
    * 
    * @param value
    *           The number of satoshis
    * @return The given value in satoshis as a string on the form "10.12345".
    */
   public static String valueString(long value) {
      return valueString(value, Denomination.BTC);
   }

   /**
    * Get the given value in satoshis as a string on the form "10.12345" using
    * the specified denomination.
    * <p>
    * This method only returns necessary decimal points to tell the exact value.
    * If you wish to display all digits use
    * {@link CoinUtil#fullValueString(long, Denomination)}
    * 
    * @param value
    *           The number of satoshis
    * @param denomination
    *           The denomination to use
    * @return The given value in satoshis as a string on the form "10.12345".
    */
   public static String valueString(long value, Denomination denomination) {
      BigDecimal d = BigDecimal.valueOf(value);
      d = d.divide(denomination.getOneUnitInSatoshis());
      return d.toPlainString();
   }

   /**
    * Get the given value in satoshis as a string on the form "10.12345000"
    * denominated in BTC.
    * <p>
    * This method always returns a string with 8 decimal points. If you only
    * wish to have the necessary digits use {@link CoinUtil#valueString(long)}
    * 
    * @param value
    *           The number of satoshis
    * @return The given value in satoshis as a string on the form "10.12345000".
    */
   public static String fullValueString(long value) {
      return fullValueString(value, Denomination.BTC);
   }

   /**
    * Get the given value in satoshis as a string on the form "10.12345000"
    * using the specified denomination.
    * <p>
    * This method always returns a string with all decimal points. If you only
    * wish to have the necessary digits use
    * {@link CoinUtil#valueString(long, Denomination)}
    * 
    * @param value
    *           The number of satoshis
    * @param denomination
    *           The denomination to use
    * @return The given value in satoshis as a string on the form "10.12345000".
    */
   public static String fullValueString(long value, Denomination denomination) {
      BigDecimal d = BigDecimal.valueOf(value);
      d = d.movePointLeft(denomination.getDecimalPlaces());
      return d.toPlainString();
   }

}
