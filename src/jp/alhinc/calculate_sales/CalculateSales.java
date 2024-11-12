package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculateSales {

	// 支店定義ファイル名
	private static final String FILE_NAME_BRANCH_LST = "branch.lst";

	// 支店別集計ファイル名
	private static final String FILE_NAME_BRANCH_OUT = "branch.out";

	// エラーメッセージ
	private static final String UNKNOWN_ERROR = "予期せぬエラーが発生しました";
	private static final String FILE_NOT_EXIST = "支店定義ファイルが存在しません";
	private static final String FILE_INVALID_FORMAT = "支店定義ファイルのフォーマットが不正です";

	/**
	 * メインメソッド
	 *
	 * @param コマンドライン引数
	 */
	public static void main(String[] args) {
		// 支店コードと支店名を保持するMap
		Map<String, String> branchNames = new HashMap<>();
		// 支店コードと売上金額を保持するMap
		Map<String, Long> branchSales = new HashMap<>();

			// 支店定義ファイル読み込み処理
			if(!readFile(args[0], FILE_NAME_BRANCH_LST, branchNames, branchSales)) {
			return;
			}

			// ※ここから集計処理を作成してください。(処理内容2-1、2-2)
			//売り上げ集計課題のファイルの中にある全てのファイルを配列にする指示

			File[] files = new File(args[0]).listFiles();

			//先にファイルの情報を格納する List(ArrayList) を宣⾔します。
			List<File> rcdFiles = new ArrayList<>();

			//全ファイル数分の処理を繰り返し行うことを指示
			for(int i = 0; i < files.length ; i++) {
				files[i].getName();
				String filenames = files[i].getName();


			//全ファイル数分の処理を繰り返し行うことを指示
				if(filenames.matches ("[0-9]{8}.+rcd$")) {
				rcdFiles.add(files[i]);
				}
			}

		for(int i = 0; i < rcdFiles.size(); i++) {

				BufferedReader br = null;

				try {
				File file = new File(rcdFiles);
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);

				} catch(IOException e) {
				System.out.println(UNKNOWN_ERROR);
				return false;
				} finally {
				// ファイルを開いている場合
				if(br != null) {
					try {
						// ファイルを閉じる
						br.close();
					} catch(IOException e) {
						System.out.println(UNKNOWN_ERROR);
						return false;
					}
				}
			}
			return true;
			}

		// 支店別集計ファイル書き込み処理
		if(!writeFile(args[0], FILE_NAME_BRANCH_OUT, branchNames, branchSales)) {
			return;
		}

	}

	/**
	 * 支店定義ファイル読み込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 読み込み可否
	 */
	private static boolean readFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		BufferedReader br = null;

		/**ファイル型のfileという変数を置き、そこにファイルパスとファイル名を代入する。
		 * File Reader型のfrという変数を置き、そこにfile(path, fileName)を代入する。
		 * BufferedReader型のbrという初期化した変数を置き、そこにfr(path, fileName)を代入する。
		 *
		 * String型のlineという変数を宣言する。
		 * lineという変数にBufferedReaderのreadLineメソッドで空欄ではない箇所を一行ずつ読み込む。
		 * String[]型の変数でitemを宣言し、そこにlineのsplitメソッドを使用してカンマ区切りにした値を代入する。
		 */
		try {
			File file = new File(path, fileName);
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while((line = br.readLine()) != null) {
				// ※ここの読み込み処理を変更してください。(処理内容1-2)
			String[] items = line.split(",");

			//Mapに追加する2つの情報を putの引数として指定します。
				branchNames.put( items[0], items[1]);
				branchSales.put( items[0], 0L);

				System.out.println(line);
			}

		} catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if(br != null) {
				try {
					// ファイルを閉じる
					br.close();
				} catch(IOException e) {
					System.out.println(UNKNOWN_ERROR);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 支店別集計ファイル書き込み処理
	 *
	 * @param フォルダパス
	 * @param ファイル名
	 * @param 支店コードと支店名を保持するMap
	 * @param 支店コードと売上金額を保持するMap
	 * @return 書き込み可否
	 */
	private static boolean writeFile(String path, String fileName, Map<String, String> branchNames, Map<String, Long> branchSales) {
		// ※ここに書き込み処理を作成してください。(処理内容3-1)

		return true;
	}

}
