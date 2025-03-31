from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny
from django.urls import get_resolver, NoReverseMatch, URLResolver
from rest_framework.reverse import reverse
from collections import OrderedDict

class ApiRootView(APIView):
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        api_urls = OrderedDict()
        resolver = get_resolver()
        
        def explore_patterns(patterns, prefix=''):
            for pattern in patterns:
                if isinstance(pattern, URLResolver):
                    # Si es un URLResolver, explorar recursivamente sus patrones
                    explore_patterns(pattern.url_patterns, prefix + str(pattern.pattern))
                else:
                    # Solo procesar patrones con nombre
                    if hasattr(pattern, 'name') and pattern.name:
                        # Saltar la vista api-root para evitar recursión
                        if pattern.name == 'api-root':
                            continue
                            
                        try:
                            # Intentar obtener la URL sin argumentos primero
                            url = reverse(pattern.name, request=request)
                            api_urls[pattern.name] = {
                                'url': url,
                                'methods': self._get_pattern_methods(pattern),
                                'description': self._get_pattern_description(pattern)
                            }
                        except NoReverseMatch:
                            # Si falla, puede ser una URL con parámetros
                            try:
                                # Proporcionar valores dummy para los parámetros
                                if '<str:username>' in str(pattern.pattern):
                                    api_urls[pattern.name] = {
                                        'url': reverse(pattern.name, kwargs={'username': 'example'}, request=request),
                                        'methods': self._get_pattern_methods(pattern),
                                        'description': self._get_pattern_description(pattern),
                                        'parameters': {
                                            'username': 'string'
                                        }
                                    }
                                elif '<int:size>' in str(pattern.pattern):
                                    api_urls[pattern.name] = {
                                        'url': reverse(pattern.name, kwargs={'size': 10}, request=request),
                                        'methods': self._get_pattern_methods(pattern),
                                        'description': self._get_pattern_description(pattern),
                                        'parameters': {
                                            'size': 'integer'
                                        }
                                    }
                                elif '<str:text>' in str(pattern.pattern):
                                    api_urls[pattern.name] = {
                                        'url': reverse(pattern.name, kwargs={'text': 'example'}, request=request),
                                        'methods': self._get_pattern_methods(pattern),
                                        'description': self._get_pattern_description(pattern),
                                        'parameters': {
                                            'text': 'string'
                                        }
                                    }
                            except NoReverseMatch:
                                # Si aún falla, registrar la ruta con información sobre los parámetros requeridos
                                api_urls[pattern.name] = {
                                    'url': str(pattern.pattern),
                                    'methods': self._get_pattern_methods(pattern),
                                    'description': 'Requires parameters',
                                    'parameters_required': True
                                }
        
        explore_patterns(resolver.url_patterns)
        return Response(api_urls)

    def _get_pattern_methods(self, pattern):
        """Obtener los métodos HTTP permitidos para el patrón"""
        if hasattr(pattern, 'callback'):
            view = pattern.callback
            if hasattr(view, 'view_class'):
                return [method.upper() for method in view.view_class.http_method_names 
                        if hasattr(view.view_class, method) and method not in ('options', 'head')]
        return ['GET']  # Valor por defecto si no se puede determinar

    def _get_pattern_description(self, pattern):
        """Obtener una descripción de la vista si está disponible"""
        if hasattr(pattern, 'callback'):
            view = pattern.callback
            if hasattr(view, '__doc__') and view.__doc__:
                return view.__doc__.strip().split('\n')[0]  # Solo la primera línea del docstring
        return ''