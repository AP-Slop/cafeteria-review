# カフェテリアレビュー

学内カフェテリアのメニューを、Google マップのレビューのように **星評価 × コメント × 写真** で共有できるアプリです。

## できること

- メニュー一覧に平均評価（★4.3 など）とレビュー件数を表示
- 「評価順」「名前順」でメニューを並べ替え
- メニュー詳細でみんなのレビューを写真付きで閲覧
- 「レビューを書く」から **5段階の星・コメント・写真** を付けて投稿
- 写真は端末のフォトピッカーから選択（権限リクエスト不要）。アプリ内に自動で縮小保存されます
- ニックネームは次回以降も引き継がれ、自分のレビューは削除できます
- 一覧画面右上の「全削除」から、投稿したレビューと写真をまとめて消せます（確認ダイアログ付き）

## 使い方

1. アプリを起動するとメニュー一覧が表示されます（唐揚げ定食・カツカレー・味噌ラーメンなど）
2. 気になるメニューをタップしてレビューを読む
3. 右下の「レビューを書く」からニックネーム・星・コメント・写真を入力して投稿
4. 投稿したレビューはすぐに一覧と平均評価に反映されます
5. すべて消したいときは一覧画面右上の「全削除」→ 確認ダイアログで「すべて削除」を選択

## データの保存場所

- レビュー: アプリ内部ストレージの `files/reviews.json`
- 写真: アプリ内部ストレージの `files/review_photos/`（長辺 1280px に縮小した JPEG）
- ニックネーム: SharedPreferences (`cafeteria_review_prefs`)

サーバーやアカウント登録は不要で、ネットワーク権限も要求しません。アプリを削除すると投稿内容も消えます。

## 構成

```
app/src/main/java/dev/apslop/apps/cafeteria_review/
  MainActivity.kt              画面の入り口と画面遷移
  data/Models.kt               MenuItem / Review
  data/Ratings.kt              平均評価・星表示・一覧要約 (純粋関数)
  data/SampleData.kt           サンプルメニューと初期レビュー
  data/ReviewStore.kt          レビューの状態管理と永続化（追加・削除・全削除）
  data/ReviewStorage.kt        JSON ファイル入出力
  data/PhotoStore.kt           写真の取り込みと縮小
  data/ProfilePrefs.kt         ニックネームの保存
  ui/Theme.kt                  配色
  ui/MenuListScreen.kt         メニュー一覧（並べ替え・全削除）
  ui/MenuDetailScreen.kt       メニュー詳細とレビュー一覧
  ui/ReviewEditorDialog.kt     レビュー投稿ダイアログ
  ui/PhotoImage.kt             端末内画像の読み込み表示
app/src/main/res/values/strings.xml   日本語 UI 文字列
app/src/test/java/dev/apslop/apps/cafeteria_review/RatingsTest.kt  ユニットテスト
```

## ビルド

```sh
./gradlew assembleDebug         # app/build/outputs/apk/debug/app-debug.apk
./gradlew installDebug          # 接続した端末にインストール
./gradlew testDebugUnitTest     # ユニットテスト
```

## リリース

`v<major>.<minor>.<patch>` 形式のタグを push すると GitHub Actions が未署名の release APK をビルドし、Release に添付します。署名は APSlop ストアが取り込み時に行います。

```sh
git tag v0.1.0
git push origin v0.1.0
```

---

このアプリは APSlop (AI app generator) によって生成されました。Kotlin + Jetpack Compose (Material3) の単一 Activity 構成です。
