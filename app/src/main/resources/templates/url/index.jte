@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage

@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <section>
        <div class="container-lg mt-5">
            <h1>Сайты</h1>
                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>Имя</th>
                        <th class="col-2">Последняя проверка</th>
                        <th class="col-1">Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var item : page.getUrls())
                        <tr>
                            <!--Страница /urls-->
                            <td>${item.getId()}</td>
                            <td><a href="${NamedRoutes.urlPath(item.getId())}">${item.getName()}</a></td>
                            @if(item.getLastCheckDate() != null)
                                <td>${item.getLastCheckDate().toString().substring(0, 19)}</td>
                            @else
                                <td></td>
                            @endif

                            @if(item.getStatus() != 0)
                                <td>${item.getStatus()}</td>
                            @else
                                <td></td>
                            @endif

                        </tr>
                    @endfor
                    </tbody>
                </table>
        </div>
    </section>
`)
