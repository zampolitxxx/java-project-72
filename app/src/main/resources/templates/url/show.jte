@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes

@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <section>
        <div class="container-lg mt-5">
            <h1>Сайт: ${page.getUrl().getName()}</h1>
                <table class="table table-bordered table-hover mt-3">
                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <td>Имя</td>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <td>Дата создания</td>
                        <td>${page.getUrl().getCreatedAt().toString().substring(0, 19)}</td>
                    </tr>
                    </tbody>
                </table>
            <h2 class="mt-5">Проверки</h2>
            <form action="${NamedRoutes.urlChecksPath(page.getUrl().getId())}" method="post">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                <tr>
                    <th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                </tr>
                </thead>
                <tbody>
                @for(var item : page.getUrlChecks())
                    <tr>
                        <td>${item.getId()}</td>
                        <td>${item.getStatusCode()}</td>
                        <td>${item.getTitle()}</td>
                        <td>${item.getH1()}</td>
                        <td>${item.getDescription()}</td>
                        <td>${item.getCreatedAt().toString().substring(0, 19)}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </section>
`)
