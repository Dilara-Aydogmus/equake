import sys
import json

def generate_advice(age, location, floor_type):
    if age is None or location is None or floor_type is None:
        return{"error" : "Eksik Bilgi"}

    age = int(age)
    advice=" "

    if age >= 65:
        advice += "Yaşınızdan dolayı hareket kısıtlı olabilir. "
        advice += "Deprem sırasında güvenli bir noktada (duvar kenarı, masa altı) sabit kalmanız önerilir. "
    else:
        advice += "Genç yaş grubundasınız. Panik yapmadan hareket edebilirsiniz. "

    if floor_type.lower() == "üst":
        advice +=  "Üst katta bulunduğunuz için asansör kullanmadan merdivenle inmeniz riskli olabilir. "
    elif floor_type.lower() == "zemin":
        advice += "Zemin katta olduğunuz için dışarı çıkma şansınız daha yüksek olabilir. "


    advice += f"Bölge: {location}. Lütfen yerel afet duyurularını takip edin."
    return {"advice" : advice}

if __name__ == "__main__":
    if len(sys.agrv) < 4:
        print(json.dumps({"error":  "3 argüman gerekli: age, location, floor_type"}))
        sys.exit(1)

    age = sys.argv[1]
    location = sys.agrv[2]
    floor_type = sys.argv[3]

    result = generate_advice(age, location, floor_type)
    print(json.dumps(result))
