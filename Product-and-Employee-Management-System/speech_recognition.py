import speech_recognition as sr


def test():
    robot_ear = sr.Recognizer()

    with sr.Microphone() as mic:

      robot_ear.adjust_for_ambient_noise(mic, duration=1)

      audio = robot_ear.listen(mic)

    try:
        you = robot_ear.recognize_google(audio)
    except sr.UnknownValueError:
        print("Robot: Sorry, I could not understand what you said.")
        you = ""
    except sr.RequestError:
        print("Robot: Sorry, there was a problem with the speech recognition service.")
        you = ""

    return you


a = test()
print(a)
