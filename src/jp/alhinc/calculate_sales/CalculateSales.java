package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
	 * @param line
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
				String fileNames = files[i].getName();

			//全ファイル数分の処理を繰り返し行うことを指示
			if(fileNames.matches ("[0-9]{8}. + rcd$")) {
				rcdFiles.add(files[i]);
			}
		}

		for(int i = 0; i < rcdFiles.size(); i++) {
				BufferedReader br = null;

			try {
				File file = new File(args[0], rcdFiles.get(i).getName());
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);

				/**
				 * 読み込んだ内容を合計していくために「型の変換（次で説明）」
				 *理由はファイルから取得した際にString型になっているが売り上げ金額は数値のため、
				 *Long型に変換してからではないとMapに格納ができないため。
				 */

				//先にファイルの情報を格納する List(ArrayList) を宣⾔します。
				List<String> fileLines = new ArrayList<>();

				String line;

				while((line = br.readLine()) != null) {
					fileLines.add(line);

				}
				//fileSaleはrcdファイルの中の2行目を読み込んだ売上金額
				//saleAmountはMapの売上金額からkeyとなる支店コードを使って取得する既存の売上金額と,
				//新たに読み込んだ売上金額を加算したもの
				String code = fileLines.get(0);
				long fileSale = Long.parseLong(fileLines.get(1));
				long saleAmount = branchSales.get(code) + fileSale;

				//codeはbranchSalesというMapのkeyにあたる支店コードを表します
				//fileSalesはrcdファイルの中の2行目の加算処理を行う売上金額を表します
				//saleAmountは既存の売上金額と加算処理を行った売り上げ金額の合計を表します
				//Mapの値を取得するためにcodeがkeyとしてvalueである既存の売上金額を呼び出します
				//MapであるbranchSalesに支店コードと集計金額を格納します
				branchSales.put(code, saleAmount);

			}catch(IOException e) {
				System.out.println(UNKNOWN_ERROR);
				return;
			}finally {
				// ファイルを開いている場合
				if(br != null) {
					try {
						// ファイルを閉じる
						br.close();
					}catch(IOException e) {
						System.out.println(UNKNOWN_ERROR);
						return;
					}
				}
			}
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

//			エラー処理①②

			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while((line = br.readLine()) != null) {
				// ※ここの読み込み処理を変更してください。(処理内容1-2)
				String[] items = line.split(",");

				//Mapに追加する2つの情報を putの引数として指定します。
				branchNames.put(items[0], items[1]);
				branchSales.put(items[0], 0L);
			}

		}catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		}finally {
			// ファイルを開いている場合
			if(br != null) {
				try {
					// ファイルを閉じる
					br.close();
				}catch(IOException e) {
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
		BufferedWriter bw = null;

//		書き出しファイルを準備する
		try {
			File file = new File(path, fileName);
			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);

			for (String key: branchNames.keySet()) {
				String branchName = branchNames.get(key);
				long branchSale = branchSales.get(key);
				bw.write(key + ","+ branchName + "," + Long.toString(branchSale));
				bw.newLine();
			}
		}catch(IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;

		}finally {
			// ファイルを開いている場合
			if(bw != null) {
				try {
					// ファイルを閉じる
					bw.close();

				}catch(IOException e) {
					System.out.println(UNKNOWN_ERROR);

					return false;
				}
			}
		}
		return true;
	}
}

