from gtts import gTTS
import os
from playsound import playsound
import sys
tts = gTTS("Số tiền đưa lại cho khách hàng là " + sys.argv[1] ,tld='com.vn',lang='vi')

tts.save("notification_by_speech1.mp3")
playsound("notification_by_speech1.mp3")