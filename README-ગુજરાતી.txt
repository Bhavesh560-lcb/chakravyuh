ચક્રવ્યુહ v38 — ફક્ત મોબાઇલ (Firebase નથી) + GitHub થી ઓટો અપડેટ
docs/ = વેબ એપ (GitHub Pages) | app/ = Android એપ | .github = APK આપમેળે બનાવે
ડેટા: ફોનમાં જ. પહેલી વાર એપ ખોલતાં નવો યુઝરનેમ-પાસવર્ડ બનાવો.
નવું વર્ઝન મૂકવા: python tools/new_version.py "ફેરફાર" → docs/ GitHub માં અપલોડ.

ડાઉનલોડ પેજ: https://bhavesh560-lcb.github.io/chakravyuh/download.html  (ખોલતાં જ APK ડાઉનલોડ)
નવી APK સાથે અપડેટ (ફોનમાં પોપઅપ + ઇન્સ્ટોલ): python tools/new_version.py --apk "ફેરફાર"
ફક્ત વેબ ફાઇલોનું અપડેટ (APK વિના): python tools/new_version.py "ફેરફાર"
