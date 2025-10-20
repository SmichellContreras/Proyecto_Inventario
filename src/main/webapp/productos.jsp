<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <title>Inventario</title>

  
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">


  <style>
    body { background:#f7f8fa; }
    .page-title { font-weight:700; letter-spacing:.3px; }
    .card { border-radius:1rem; border:1px solid #e9ecef; }
    .table thead th { background:#0d6efd; color:#fff; border-color:#0b5ed7; }
    .table-hover tbody tr:hover { background:#f3f7ff; }
    .form-label { font-weight:600; }
    .btn-pill { border-radius:999px; }
    .toast-container { z-index: 1080; } /* sobre el contenido y el modal */
  </style>
</head>
<body>

<div class="container py-4">

  <div class="d-flex align-items-center justify-content-between mb-3">
    <h1 class="page-title mb-0">Inventario</h1>
  </div>

  <!-- MENSAJES (Toasts Bootstrap) -->
  <div class="toast-container position-fixed top-0 end-0 p-3">
    <c:if test="${not empty error or not empty errorListado or (not empty mensajeBean and not empty mensajeBean.error)}">
      <div class="toast align-items-center text-bg-danger border-0" role="alert" aria-live="assertive" aria-atomic="true" id="toastError">
        <div class="d-flex">
          <div class="toast-body">
            <c:out value="${error != null ? error : (errorListado != null ? errorListado : mensajeBean.error)}"/>
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
      </div>
    </c:if>

    <c:if test="${not empty mensajeBean and not empty mensajeBean.info}">
      <div class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true" id="toastInfo">
        <div class="d-flex">
          <div class="toast-body">
            <c:out value="${mensajeBean.info}"/>
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
      </div>
    </c:if>
  </div>

  <!-- LISTADO -->
  <div class="card mb-4">
    <div class="card-header bg-white">
      <h5 class="mb-0">Listado</h5>
    </div>
    <div class="card-body p-0">
      <div class="table-responsive">
        <table class="table table-hover mb-0 align-middle">
          <thead>
          <tr>
            <th>ID</th>
            <th>Código</th>
            <th>Nombre</th>
            <th>Categoría</th>
            <th class="text-end">Precio</th>
            <th class="text-end">Stock</th>
            <th class="text-center">Activo</th>
            <th class="text-center">Acción</th>
          </tr>
          </thead>
          <tbody>
          <c:choose>
            <c:when test="${empty productos}">
              <tr>
                <td colspan="8" class="text-center text-muted py-4">No hay productos</td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="p" items="${productos}">
                <tr>
                  <td>${p.id}</td>
                  <td class="fw-semibold">${p.codigo}</td>
                  <td>${p.nombre}</td>
                  <td><span class="badge text-bg-light border">${p.categoria}</span></td>
                  <td class="text-end">${p.precio}</td>
                  <td class="text-end">${p.stock}</td>
                  <td class="text-center">
                    <c:choose>
                      <c:when test="${p.activo}"><span class="badge text-bg-success">Sí</span></c:when>
                      <c:otherwise><span class="badge text-bg-secondary">No</span></c:otherwise>
                    </c:choose>
                  </td>
                  <td class="text-center">
                    <!-- Botón EDITAR: abre modal y pasa datos via data-* -->
                    <button type="button"
                            class="btn btn-sm btn-primary btn-pill"
                            data-bs-toggle="modal" data-bs-target="#editModal"
                            data-id="${p.id}"
                            data-codigo="${p.codigo}"
                            data-nombre="${p.nombre}"
                            data-categoria="${p.categoria}"
                            data-precio="${p.precio}"
                            data-stock="${p.stock}"
                            data-activo="${p.activo}">
                      Editar
                    </button>
                    <span class="mx-1"></span>
                    <!-- Eliminar con confirm simple -->
                    <a class="btn btn-sm btn-outline-danger btn-pill"
                       href="${pageContext.request.contextPath}/productos?action=del&id=${p.id}"
                       onclick="return confirm('¿Eliminar ${p.codigo}?');">
                      Eliminar
                    </a>
                  </td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
          </tbody>
        </table>
      </div>
    </div>
  </div>

  <!-- CREAR -->
  <div class="card">
    <div class="card-header bg-white">
      <h5 class="mb-0">Nuevo producto</h5>
    </div>
    <div class="card-body">
      <form method="post" action="${pageContext.request.contextPath}/productos" class="row g-3">
        <div class="col-md-4">
          <label class="form-label">Código</label>
          <input class="form-control" name="codigo" value="${form.codigo}">
        </div>
        <div class="col-md-8">
          <label class="form-label">Nombre</label>
          <input class="form-control" name="nombre" value="${form.nombre}">
        </div>
        <div class="col-md-6">
          <label class="form-label">Categoría</label>
          <input class="form-control" name="categoria" value="${form.categoria}">
        </div>
        <div class="col-md-3">
          <label class="form-label">Precio</label>
          <input class="form-control" name="precio" value="${form.precio}">
        </div>
        <div class="col-md-3">
          <label class="form-label">Stock</label>
          <input class="form-control" name="stock" value="${form.stock}">
        </div>
        <div class="col-12">
          <div class="form-check">
            <input class="form-check-input" type="checkbox" id="crearActivo" name="activo"
                   <c:if test="${form.activo}">checked</c:if> >
            <label class="form-check-label" for="crearActivo">Activo</label>
          </div>
        </div>
        <div class="col-12">
          <button type="submit" class="btn btn-success btn-pill">Crear</button>
        </div>
      </form>
    </div>
  </div>

</div> <!-- /container -->

<!-- MODAL DE EDICIÓN -->
<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content rounded-4">
      <div class="modal-header">
        <h5 class="modal-title" id="editModalLabel">Editar producto</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
      </div>
      <form method="post" action="${pageContext.request.contextPath}/productos">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" id="edit-id">

        <div class="modal-body">
          <div class="mb-3">
            <label class="form-label">Código</label>
            <input class="form-control" name="codigo" id="edit-codigo">
          </div>
          <div class="mb-3">
            <label class="form-label">Nombre</label>
            <input class="form-control" name="nombre" id="edit-nombre">
          </div>
          <div class="mb-3">
            <label class="form-label">Categoría</label>
            <input class="form-control" name="categoria" id="edit-categoria">
          </div>
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Precio</label>
              <input class="form-control" name="precio" id="edit-precio">
            </div>
            <div class="col-md-6">
              <label class="form-label">Stock</label>
              <input class="form-control" name="stock" id="edit-stock">
            </div>
          </div>
          <div class="form-check mt-3">
            <input class="form-check-input" type="checkbox" id="edit-activo" name="activo">
            <label class="form-check-label" for="edit-activo">Activo</label>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn btn-outline-secondary btn-pill" data-bs-dismiss="modal">Cancelar</button>
          <button type="submit" class="btn btn-primary btn-pill">Guardar cambios</button>
        </div>
      </form>
    </div>
  </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL"
        crossorigin="anonymous"></script>
<script>
  // Rellenar el modal con los data-* del botón Editar
  const editModal = document.getElementById('editModal');
  editModal.addEventListener('show.bs.modal', event => {
    const btn = event.relatedTarget; // botón que abrió el modal
    const id = btn.getAttribute('data-id');
    const codigo = btn.getAttribute('data-codigo');
    const nombre = btn.getAttribute('data-nombre');
    const categoria = btn.getAttribute('data-categoria');
    const precio = btn.getAttribute('data-precio');
    const stock = btn.getAttribute('data-stock');
    const activo = btn.getAttribute('data-activo') === 'true';

    document.getElementById('edit-id').value = id;
    document.getElementById('edit-codigo').value = codigo ?? '';
    document.getElementById('edit-nombre').value = nombre ?? '';
    document.getElementById('edit-categoria').value = categoria ?? '';
    document.getElementById('edit-precio').value = precio ?? '';
    document.getElementById('edit-stock').value = stock ?? '';
    document.getElementById('edit-activo').checked = activo;
  });

  const toastError = document.getElementById('toastError');
  const toastInfo  = document.getElementById('toastInfo');
  if (toastError) new bootstrap.Toast(toastError, {delay: 5000}).show();
  if (toastInfo)  new bootstrap.Toast(toastInfo,  {delay: 3500}).show();
</script>
</body>
</html>
