このファイルには，要旨用レイアウト設定ファイルの構成が記載されています．

apulayout.zip を解凍すると，

README.1st.SJIS.txt -----> このファイル
README.1st.UTF8.txt -----> このファイルと同じ内容（UTF8版）
README.1st.EUC.txt  -----> このファイルと同じ内容（EUC版）
apulayout.sty       -----> 要旨レイアウト設定ファイル（UTF8版）
figsample.eps       -----> 利用マニュアルで使用する図
bachelor.tex        -----> 学部生用の利用マニュアル・ソース（UTF8版）
master.tex          -----> 院生用の利用マニュアル・ソース（UTF8版）
supp/               -----> スタイルファイル
SJIS/               -----> 上の4つのファイルのSJIS 版
EUC/                -----> 上の4つのファイルのEUC 版

supp/ の内容
 geometry.sty       -----> レイアウト設定で使用するスタイル
 nidanfloat.sty     -----> 二段抜きをするためのスタイル

の7つのファイルと3つのディレクトリが生成されます．

geometry.sty と nidanfloat.sty は，標準の LaTeX	環境にあるとは思いますが，
念のためsuppディレクトリの下に同梱しました．
! LaTeX Error: File `nidanfloat.sty' not found.
のようなエラーが出るときには，これらのファイルをカレントディレクトリにコピーして使ってください．

bachelor.tex，master.tex は，要旨レイアウトの利用方法について説明しています．

platex bachelor.tex
platex bachelor.tex
dvipdfmx bachelor.dvi

または，

platex master.tex
platex master.tex
dvipdfmx master.dvi

と実行して，利用マニュアルを生成して下さい．
このマニュアル・ソースも抄録レイアウトを使用しているので参考になるかも知れません． 

SJIS, EUSディレクトリ以下には，SJIS, EUSコードのファイルが配置されています．
UTF8コード版で正常に動作しない場合に試してみて下さい．

----------------------------------------------------------------------
文書，レイアウト作成：戸田研究室 兼弘
改ざん：太田淳
