package jp.alhinc.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private static final String FILENAME_NOT_CONSECUTIVE = "売上ファイル名が連番になっていません";
	private static final String TOTOTAL_AMOUNT_OVER_10 = "合計金額が10桁を超えました";
	private static final String VARIABLE_FILE_INVALID_CODE = "の支店コードが不正です";
	private static final String VARIABLE_FILE_INVALID_FORMAT = "のフォーマットが不正です";

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

//		エラー3‐1
		if (args.length != 1) {
		    //コマンドライン引数が1つ設定されていなかった場合は、
		    //エラーメッセージをコンソールに表示します。
			System.out.println(UNKNOWN_ERROR);
			return;
		}

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
			//エラー3‐2
			//対象がファイルであり、「数字8桁.rcd」なのか判定します。
			if(files[i].isFile() && fileNames.matches("[0-9]{8}.+rcd$")) {
				rcdFiles.add(files[i]);
			}
		}

//		エラー2-1(売上ファイルが8桁の数字で連番になっているかの確認)
		Collections.sort(rcdFiles);
		for(int i = 0; i < rcdFiles.size() - 1; i++) {
			int former = Integer.parseInt(rcdFiles.get(i).getName().substring(0, 8));
			int latter = Integer.parseInt(rcdFiles.get(i + 1).getName().substring(0, 8));
			if((latter - former) != 1) {
				System.out.println(FILENAME_NOT_CONSECUTIVE);
				return;
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

				if(fileLines.size() != 2) {
				    //売上ファイルの行数が2行ではなかった場合は、
				    //エラーメッセージをコンソールに表示します。
					System.out.println(rcdFiles.get(i).getName()  + VARIABLE_FILE_INVALID_FORMAT);
					return;
				}

				String code = fileLines.get(0);

//				エラー3－3
				if(!fileLines.get(1).matches("^[0-9]*$")) {
				    //売上金額が数字ではなかった場合は、
				    //エラーメッセージをコンソールに表⽰します。
					System.out.println(UNKNOWN_ERROR);
					return;
				}

				long fileSale = Long.parseLong(fileLines.get(1));


				if (!branchNames.containsKey(code)) {
				    //支店情報を保持しているMapに売上ファイルの支店コードが存在しなかった場合は、
				    //エラーメッセージをコンソールに表示します。
					System.out.println(rcdFiles.get(i).getName() + VARIABLE_FILE_INVALID_CODE);
					return;
				}

				long saleAmount = branchSales.get(code) + fileSale;


//				エラー処理2-2
//				合計金額が10桁を超えた場合、エラーメッセージ「合計金額が10桁を超えました」を表示し、処理を終了する。

				if(saleAmount >= 10000000000L){
//					売上金額が11桁以上の場合、エラーメッセージをコンソールに表示します。
					System.out.println(TOTOTAL_AMOUNT_OVER_10);
					return;
				}

				//codeはbranchSalesというMapのkeyにあたる支店コードを表します
				//fileSalesはrcdファイルの中の2行目の加算処理を行う売上金額を表します
				//saleAmountは既存の売上金額と加算処理を行った売り上げ金額の合計を表します
				//Mapの値を取得するためにcodeがkeyとしてvalueである既存の売上金額を呼び出します
				//MapであるbranchSalesに支店コードと集計金額を格納します
				branchSales.put(code, saleAmount);

			} catch (IOException e) {
				System.out.println(UNKNOWN_ERROR);
				return;
			} finally {
				// ファイルを開いている場合
				if(br != null) {
					try {
						// ファイルを閉じる
						br.close();
					} catch (IOException e) {
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

			/**エラー処理1-1
			*支店定義ファイルが存在しない場合は、エラーメッセージ「支店定義ファイルが存在しません」を表示させたい
			*/

			if(!file.exists()) {
				System.out.println(FILE_NOT_EXIST);
				return false;
			}

			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);

			String line;
			// 一行ずつ読み込む
			while((line = br.readLine()) != null) {
				// ※ここの読み込み処理を変更してください。(処理内容1-2)
				String[] items = line.split(",");

				/**エラー処理1-2
				*支店定義ファイルのフォーマットが不正な場合は、エラーメッセージ「支店定義ファイルのフォーマットが不正です」を表示したい
				*/
				if((items.length != 2) || (!items[0].matches("^[0-9]{3}"))) {
					System.out.println(FILE_INVALID_FORMAT);
					return false;
				}

				//Mapに追加する2つの情報を putの引数として指定します。
				branchNames.put(items[0], items[1]);
				branchSales.put(items[0], 0L);
			}

		} catch (IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;
		} finally {
			// ファイルを開いている場合
			if(br != null) {
				try {
					// ファイルを閉じる
					br.close();
				} catch (IOException e) {
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
		} catch (IOException e) {
			System.out.println(UNKNOWN_ERROR);
			return false;

		} finally {
			// ファイルを開いている場合
			if(bw != null) {
				try {
					// ファイルを閉じる
					bw.close();

				} catch (IOException e) {
					System.out.println(UNKNOWN_ERROR);

					return false;
				}
			}
		}
		return true;
	}
}

