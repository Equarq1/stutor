<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:layout title="${pageTitle}">
    <main>
        <h2><c:out value="${pageTitle}"/></h2>

        <div class="requests-tabs">
            <a href="${pageContext.request.contextPath}/requests?type=outgoing">Мои отклики</a>
            <a href="${pageContext.request.contextPath}/requests?type=incoming">Входящие мне</a>
        </div>

        <div class="offer-cards">
            <c:choose>
                <c:when test="${not empty requests}">
                    <c:forEach var="reqInfo" items="${requests}">
                        <div class="card">
                            <h3>На объявление: <a href="${pageContext.request.contextPath}/offer?id=${reqInfo.offer.id}"><c:out value="${reqInfo.offer.title}"/></a></h3>
                            <p><strong>Предмет:</strong> <c:out value="${reqInfo.subject.name}"/></p>

                            <c:if test="${isIncoming}">
                                <p><strong>От ученика:</strong> <a href="${pageContext.request.contextPath}/user?id=${reqInfo.student.id}"><c:out value="${reqInfo.student.fullName}"/></a></p>
                            </c:if>

                            <p><strong>Сообщение:</strong> <c:out value="${reqInfo.message}"/></p>
                            <p><strong>Статус:</strong>
                                <span class="status-${reqInfo.status}">
                                        ${reqInfo.status}
                                </span>
                            </p>

                            <c:if test="${not empty reqInfo.attachment}">
                                <p><a href="${pageContext.request.contextPath}/file?id=${reqInfo.attachment.id}">📥 Скачать прикрепленный файл</a></p>
                            </c:if>

                            <c:if test="${isIncoming && reqInfo.status == 'PENDING'}">
                                <div class="request-actions">
                                    <form class="inline-form" action="${pageContext.request.contextPath}/request/status" method="post">
                                        <input type="hidden" name="requestId" value="${reqInfo.id}">
                                        <input type="hidden" name="status" value="ACCEPTED">
                                        <button type="submit" class="btn-accept">Принять</button>
                                    </form>
                                    <form class="inline-form" action="${pageContext.request.contextPath}/request/status" method="post">
                                        <input type="hidden" name="requestId" value="${reqInfo.id}">
                                        <input type="hidden" name="status" value="REJECTED">
                                        <button type="submit" class="btn-reject">Отклонить</button>
                                    </form>
                                </div>
                            </c:if>

                            <c:if test="${isIncoming && reqInfo.status == 'ACCEPTED'}">
                                <p><strong>Контакт ученика:</strong> <c:out value="${reqInfo.student.contact}"/></p>
                            </c:if>

                            <c:if test="${not isIncoming}">
                                <p><strong>Репетитор:</strong>
                                    <a href="${pageContext.request.contextPath}/user?id=${reqInfo.offer.author.id}">
                                        <c:out value="${reqInfo.offer.author.fullName}"/>
                                    </a>
                                </p>
                            </c:if>

                            <c:if test="${not isIncoming and reqInfo.status == 'ACCEPTED'}">
                                <p>
                                    <strong>Вам стали доступны контакты:</strong>
                                    <c:out value="${not empty reqInfo.offer.author.contact ? reqInfo.offer.author.contact : 'Репетитор не указал контакты, ждите пока он сам с вами свяжется или следите не появились ли контакты'}"/>
                                </p>
                                <a href="${pageContext.request.contextPath}/review/create?requestId=${reqInfo.id}" class="button">Оставить отзыв</a>
                            </c:if>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p>Список заявок пуст.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </main>
</t:layout>