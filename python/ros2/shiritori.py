import random
import nltk
from nltk.corpus import wordnet as wn

# Japanese WordNetを使うための設定
nltk.download('omw-1.4')
nltk.download('wordnet')

# 日本語WordNetを使用する
wn.langs()
wn.synsets('猫', lang='jpn')

def shiritori(start_letter):
    # 日本語の単語を取得
    japanese_words = set(lemma.name() for synset in wn.all_synsets(lang='jpn') for lemma in synset.lemmas(lang='jpn'))
    
    # 指定された文字で始まる単語を探す
    candidates = [word for word in japanese_words if word.startswith(start_letter)]
    
    # 候補が見つからなければNoneを返す
    if not candidates:
        return None
    
    # ランダムに単語を選択して返す
    return random.choice(candidates)

# しりとりを始める例
start_letter = input("ひらがな一文字 : ")
result = shiritori(start_letter)

if result:
    print(f"見つかった単語: {result}")
else:
    print("指定された文字で始まる単語が見つかりませんでした。")
