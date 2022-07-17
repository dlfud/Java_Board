
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("== 게시판 v 0.1 ==");
        System.out.println("== 프로그램 시작 ==");

        int articlesLastId = 0;
        List<Article> articles = new ArrayList<>();

        makeTestData(articles);

        if(articles.size() > 0){
            articlesLastId = articles.get(articles.size() - 1).id;
        }

        while(true) {
            System.out.print("명령) ");
            String cmd = sc.nextLine();

            Rq rq = new Rq(cmd);

            if(rq.getUrlPath().equals("exit")){
                break;
            }else if (rq.getUrlPath().equals("/usr/article/list")){
                System.out.println("- 게시물 리스트 -");
                System.out.println("--------------------");
                System.out.println("번호 / 제목");
                System.out.println("--------------------");

                for(int i = articles.size() - 1; i >= 0; i--){
                    Article article = articles.get(i);
                    System.out.printf("%d / %s\n", article.id, article.title);
                }
            }else if (rq.getUrlPath().equals("/usr/article/detail")){
                if(articles.isEmpty()){
                    System.out.println("게시물이 존재하지 않습니다.");
                    continue;
                }

                Article article = articles.get(articles.size() - 1);

                System.out.println("- 게시물 상세내용 -");
                System.out.printf("번호 : %d\n", article.id);
                System.out.printf("제목 : %s\n", article.title);
                System.out.printf("내용 : %s\n", article.body);
            }else if (rq.getUrlPath().equals("/usr/article/write")) {
                System.out.println("- 게시물 등록 -");

                System.out.printf("제목 : ");
                String title = sc.nextLine();

                System.out.printf("내용 : ");
                String body = sc.nextLine();

                int id = articlesLastId + 1;
                articlesLastId = id;

                Article article = new Article(id, title, body);
                articles.add(article);

                System.out.println("생성된 게시물 객체 : " + article);

                System.out.printf("%d번 게시물이 입력되었습니다.\n", article.id);
            }else{
                System.out.printf("입력된 명령어 : %s\n", cmd);
            }
        }
        System.out.println("== 프로그램 종료 ==");

        sc.close();
    }

    static void makeTestData(List<Article> articles){
        articles.add(new Article(1, "제목1", "내용1"));
        articles.add(new Article(2, "제목2", "내용2"));
        articles.add(new Article(3, "제목3", "내용3"));
    }
}

class Article {
    int id;
    String title;
    String body;

    Article(int id, String title, String body){
        this.id = id;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return String.format("{id : %d, titld : \"%s\", body : \"%s\"}", id, title, body);
    }
}

class Rq{
    private String url;
    private String urlPath;
    private Map<String, String> params;

    Rq(String url){
        this.url = url;
        urlPath = Util.getUrlPathFromUrl(this.url);
        params = Util.getParamsFromUrl(this.url);
    }

    public Map<String, String> getParams(){
        return params;
    }

    public String getUrlPath(){
        return urlPath;
    }
}

class Util {
    static Map<String, String> getParamsFromUrl(String url) {
        Map<String, String> params = new HashMap<>();
        String[] urlBits = url.split("\\?", 2); // '?'기준으로 나눔

        if (urlBits.length == 1) {
            return params;
        }

        String queryStr = urlBits[1];
        for (String bit : queryStr.split("&")) { // '&' 기준으로 나눈 것을 차례대로 bit에 넣음
            String[] bits = bit.split("=", 2); // '=' 기준으로 나눈것을 bits배열에 넣음
            if (bits.length == 1) {
                continue;
            }
            params.put(bits[0], bits[1]); // map에 넣음
        }

        return params;//param 리턴
    }

    static String getUrlPathFromUrl(String url) {
        return url.split("\\?", 2)[0]; // '?' 기준으로 나눈 것을 리턴
    }
}