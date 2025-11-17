# EduSocial Backend - Red Social Educativa

## 🎯 Objetivo
Backend minimalista de una red social educativa que implementa patrones de diseño del curso.

## 📁 Estructura del Proyecto

```
src/main/java/com/dpatrones/proyecto/
├── domain/model/              # Entidades JPA
│   ├── User.java             # Usuario (ya existía)
│   ├── Role.java             # Rol (ya existía)
│   ├── Post.java             # Posts con Estado (State Pattern)
│   ├── Note.java             # Apuntes personales
│   ├── Comment.java          # Comentarios
│   ├── Interaction.java      # Likes, Follows
│   └── NoteLink.java         # Enlaces entre notas
│
├── design/                   # Patrones de Diseño
│   ├── singleton/
│   │   └── ConfigService.java        # SINGLETON
│   ├── factory/
│   │   └── PostFactory.java          # FACTORY
│   ├── observer/
│   │   ├── NotificationObserver.java # OBSERVER
│   │   └── PostEventManager.java
│   ├── decorator/
│   │   ├── PostContent.java          # DECORATOR
│   │   ├── BasicPost.java
│   │   ├── PostDecorator.java
│   │   ├── MentionDecorator.java
│   │   └── HashtagDecorator.java
│   └── state/
│       ├── PostState.java            # STATE
│       ├── DraftState.java
│       └── PublishedState.java
│
├── service/                  # Servicios (GRASP)
│   ├── PostService.java      # Experto en Posts
│   └── NoteService.java      # Experto en Notas
│
└── repository/               # Data Access
    ├── PostRepository.java
    ├── NoteRepository.java
    └── CommentRepository.java
```

## 🎓 Patrones Implementados

### 1. **SINGLETON** (ConfigService)
- Asegura una única instancia de configuración
- Controla limites de tamaño de posts/notas
- Ubicación: `design/singleton/ConfigService.java`

### 2. **FACTORY** (PostFactory)
- Crea diferentes tipos de Posts (BLOG, QUESTION, RESOURCE)
- Encapsula la lógica de creación
- Ubicación: `design/factory/PostFactory.java`

### 3. **OBSERVER** (PostEventManager)
- Notifica cuando ocurren eventos (comentarios, likes)
- Desacoplamiento entre productores y consumidores
- Ubicación: `design/observer/`

### 4. **DECORATOR** (Post con menciones/hashtags)
- Enriquece Posts con decoraciones
- Composición sobre herencia
- Ubicación: `design/decorator/`

### 5. **STATE** (Estados del Post)
- DRAFT → PUBLISHED → ARCHIVED
- Cambia comportamiento según estado
- Ubicación: `design/state/`

## 🏗️ Principios GRASP Aplicados

- **Experto en Información**: PostService (sabe sobre Posts), NoteService (sabe sobre Notas)
- **Controlador**: PostService orquesta operaciones
- **Creador**: PostFactory crea Posts
- **Bajo Acoplamiento**: Servicios inyectados con @RequiredArgsConstructor
- **Alta Cohesión**: Cada clase tiene una responsabilidad

## 📊 Entidades Principales

### Post (Posts de la comunidad)
- ID, Autor (User), Título, Contenido
- Estado: DRAFT, PUBLISHED, ARCHIVED
- Tipo: BLOG, QUESTION, RESOURCE
- Likes, Fechas

### Note (Apuntes personales)
- ID, Dueño (User), Título, Contenido
- Asignatura (Matemáticas, Programación, etc.)
- Versionado automático
- Enlaces entre notas (como Obsidian)

### Comment (Comentarios)
- ID, Post, Autor, Contenido, Fecha

### Interaction (Likes, Follows)
- LIKE: alguien le gustó tu post
- FOLLOW: alguien te sigue
- SHARE: alguien compartió tu post

## 🚀 Cómo Usar

```java
// SINGLETON - Obtener configuración
ConfigService config = ConfigService.getInstance();
int maxLength = config.getMaxPostLength();

// FACTORY - Crear diferentes tipos de posts
Post blog = PostFactory.createBlogPost(user, "Título", "Contenido");
Post question = PostFactory.createQuestionPost(user, "¿Cómo...?", "Detalles");

// OBSERVER - Suscribirse a eventos
eventManager.subscribe(miObservador);
eventManager.notifyCommentAdded("Mi Post", "Juan");

// DECORATOR - Enriquecer contenido
PostContent basico = new BasicPost("Mi contenido");
PostContent conMenciones = new MentionDecorator(basico);
PostContent conTodo = new HashtagDecorator(conMenciones);

// SERVICE - Operaciones
postService.createBlogPost(user, "Título", "Contenido");
postService.publishPost(1L);
noteService.createNote(user, "Apunte", "Contenido", "Programación");
```

## 📚 Repositorio

El código está estructura para ser presentado como Proyecto Final de Diseño de Patrones.

---

**Alumno**: [Tu Nombre]  
**Curso**: Diseño de Patrones  
**Fecha**: Noviembre 2025
