@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@import java.time.format.DateTimeFormatter

@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <h1 class="display-5 mb-5">Сайт: ${page.getUrl().getName()}</h1>
    <div class="table-responsive">
        <table class="table table-dark table-hover">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${page.getUrl().getCreatedAt().toString()}</td>
            </tr>
            </tbody>
        </table>
    </div>
    <h2 class="display-5 mt-5 mb-3">Проверки</h2>
    <form action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" method="post" class="rss-form text-body">
        <button type="submit" class="btn btn-lg btn-primary mt-2">Запустить проверку</button>
    </form>
    <div class="table-responsive">
        <table class="table table-dark table-hover">
            <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Дата проверки</th>
                    <th scope="col">Код ответа</th>
                </tr>
            </thead>
            <tbody>
            @for(var item : page.getUrlChecks())
                <tr>
                    <td>${item.getId()}</td>
                    <td>${item.getCreatedAt().toString()}</td>
                    <td>${item.getStatusCode()}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    </div>
`)